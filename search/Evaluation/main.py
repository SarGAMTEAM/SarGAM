import time
from os import path
from tfidf import TfIdfEvaluator
from plbart import PlBartEvaluator
from bm25 import BM25Evaluator

from experiments import variations
from utils import set_filepaths, get_file_absolute_location
import logging

func_arguments = {}

def compute_similarity_matrix(obj, method):
    """
    Function to compute similarity matrix using evaluator obj method.

    :param obj: Evaluator Object
    :param method: plbart or tfidf
    :return: torch top k results
    """

    similarity_matrix_filename = 'similarity_matrix' + obj.create_filename() + '_' + method + '.npy'
    similarity_matrix_filename = get_file_absolute_location(obj.query_folder_location,
                                                            similarity_matrix_filename)
    start_time = time.time()
    top_k_similarity_matrix = obj.get_top_k_similarity_matrix(similarity_matrix_filename)
    print('Obtained top k results', time.time() - start_time)

    return top_k_similarity_matrix


def compute_similarity_matrix_and_edit_dist(obj, method):
    """
    Call similarity_matrix method to get top k results.
    Call objects edit distance method to calculate edit distance
    and plot distribution.

    :param obj: Evaluator Object
    :param method: plbart or tfidf
    :return: None
    """

    top_k_results = compute_similarity_matrix(obj, method)
    obj.calculate_edit_distance(top_k_results)

    return None


def compute_similarity_matrix_and_map_k_results(obj, method):
    """
    Call similarity_matrix method to get top k results.
    Call objects map k results to create dataset

    :param obj: Evaluator Object
    :param method: plbart or tfidf
    :return: None
    """

    top_k_results = compute_similarity_matrix(obj, method)
    obj.create_retrieved_fixed_dataset(top_k_results)

    return None

def compute_similarity_matrix_and_edit_dist_and_map_k_results(obj, method):

    top_k_results = compute_similarity_matrix(obj, method)
    obj.calculate_edit_distance(top_k_results)
    obj.create_retrieved_fixed_dataset(top_k_results)


def evaluate(dataset_size,
             src_lang, tgt_lang,
             db_path, query_path,
             db_data_filename,
             query_filename,
             k,
             concatenate,
             method,
             model_ckpt,
             ):
    """
    Create Evaluator Object
    Set File paths to read from
    Load Queries and DB
    Find Top-K similar results from DB for each Query

    :param dataset_size: directory from which to load datasets from
    :param src_lang: plbart source language
    :param tgt_lang: plbart target language
    :param query_path: query folder name
    :param db_path: database folder name
    :param db_data_filename: db filename to use for comparison
    :param query_filename: query filename to use for comparison
    :param k: denote the top-k similar data from db for each query
    :param concatenate: if reading from multiple data sources and concatenation is required
    :param method: plbart or tfidf
    :return: None
    """
    if method == 'plbart':
        evaluator_obj = PlBartEvaluator(dataset_size,
                                        src_lang, tgt_lang,
                                        db_data_filename,
                                        query_filename,
                                        k,
                                        concatenate)

    elif method == 'tfidf':
        evaluator_obj = TfIdfEvaluator(dataset_size,
                                       None,
                                       None,
                                       db_data_filename,
                                       query_filename,
                                       k,
                                       concatenate)
    elif method == 'modit':
        evaluator_obj = PlBartEvaluator(dataset_size,
                                        src_lang, tgt_lang,
                                        db_data_filename,
                                        query_filename,
                                        k,
                                        concatenate,
                                        model_ckpt=model_ckpt)
    elif method == 'bm25':
        evaluator_obj = BM25Evaluator(dataset_size,
                                      None,
                                      None,
                                      db_data_filename,
                                      query_filename,
                                      k,
                                      concatenate)
    else:
        print('Evaluator Method Undefined ', method)
        return None

    set_filepaths(evaluator_obj, path.dirname(__file__), dataset_size, db_path, query_path)
    # device = "cuda:0" if torch.cuda.is_available() else "cpu"
    # evaluator_obj.device = device

    evaluator_obj.set_data()

    compute_similarity_matrix_and_edit_dist_and_map_k_results(evaluator_obj, method)
    # del evaluator_obj.tokenizer
    # del evaluator_obj.model
    # compute_similarity_matrix_and_map_k_results(evaluator_obj, method)
    return None


def main():
    """
    Variations for testing
    Saves all variation results
    :return: None
    """
    logging.basicConfig(filename='bm25_variations.log', filemode='w', level=logging.INFO)
    for variation in variations:
        logging.info('Running variation %s', variation)
        global func_arguments
        func_arguments = variation
        evaluate(**func_arguments)
        logging.info('Completed variation %s', variation)


if __name__ == '__main__':
    main()
