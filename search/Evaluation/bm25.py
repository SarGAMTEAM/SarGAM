from rank_bm25 import BM25Okapi
from utils import Evaluator, concatenate_data
from os.path import exists
import pickle
import torch
import numpy as np
import re

class BM25Evaluator(Evaluator):

    def __init__(self, dataset_size, src_lang, tgt_lang, db_data_filename, query_filename, k, concatenate):
        super().__init__(dataset_size, src_lang, tgt_lang, db_data_filename, query_filename, k, concatenate)

    def set_data(self):
        super().set_data()

        if self.concatenate:
            db_data = concatenate_data(self.db_data)
            queries = concatenate_data(self.queries)
            self.tokenized_db_data = [self.bm25_preprocess(data) for
                                      data in db_data]
            self.tokenized_queries = [self.bm25_preprocess(data) for
                                      data in queries]

        else:
            self.tokenized_db_data = [self.bm25_preprocess(data) for
                                      data in self.db_data]
            self.tokenized_queries = [self.bm25_preprocess(data) for
                                      data in self.queries]

    def bm25_preprocess(self, data):
        return re.sub(' +', ' ', data).strip().split()

    def get_top_k_similarity_matrix(self,
                                    filename):
        if exists(filename):
            print(filename + 'exists')
            with open(filename, 'rb') as f:
                bm25_obj = pickle.load(f)

        else:
            bm25_obj = BM25Okapi(self.tokenized_db_data)
            with open(filename, 'wb') as f:
                pickle.dump(bm25_obj, f)

        similarity_matrix = []
        for query in self.tokenized_queries:
            similarity_matrix.append(bm25_obj.get_scores(query))
        similarity_matrix = np.vstack(similarity_matrix)
        similarity_matrix = torch.from_numpy(similarity_matrix)
        top_k_similarity_matrix = torch.topk(similarity_matrix, self.k, dim=1)

        self.top_k_similarity_matrix = top_k_similarity_matrix
        return top_k_similarity_matrix

    def calculate_edit_distance(self, top_k_similarity_matrix):
        """
        Call Parent class function and pass specific filename

        :param top_k_similarity_matrix: top-k results from get_top_k_similarity_matrix
        :return: None
        """
        edit_distance_filename = 'edit_distances' + self.create_filename_with_k() + '_bm25'
        norm_edit_distance_filename = 'norm_edit_distances' + self.create_filename_with_k() + '_bm25'
        visualization_filename = 'visualization' + self.create_filename_with_k() + '_bm25'
        super().calculate_edit_distance(top_k_similarity_matrix,
                                        edit_distance_filename,
                                        norm_edit_distance_filename,
                                        visualization_filename)

        return None

    def create_retrieved_fixed_dataset(self,
                                       top_k_similarity_matrix,
                                       ):

        """
        Call Parent class function and pass specific filename

        :param top_k_similarity_matrix: top-k results from get_top_k_similarity_matrix
        :return: None
        """
        retrieved_dataset_filename = 'retrieved' + self.create_filename_with_k() + '_bm25'
        super().create_retrieved_fixed_dataset(top_k_similarity_matrix,
                                               retrieved_dataset_filename,
                                               )

        return None

