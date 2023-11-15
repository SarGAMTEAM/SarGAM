import matplotlib.pyplot as plt
import nltk
from os import path
from os.path import exists
import seaborn as sns
import numpy as np
import json



def concatenate_data(data, token=' <b> '):
    """
    Concatenate data when reading from multiple sources

    :param data: list of data content
    :param token: token to be append after each source
    :return: list of strings
    """

    if len(data) == 2:
        return [x + token + y for x, y in zip(data[0], data[1])]
    elif len(data) == 3:
        return [x + token + y + token + z for x, y, z in zip(data[0], data[1], data[2])]


def set_filepaths(obj, basepath, dataset_size, db_path, query_path):
    """
    Function to set filepaths in evaluator object.

    :param obj: Evaluator object
    :param basepath: Path Directory
    :param dataset_size: small or large dataset
    :param db_path: database folder name
    :param query_path: query folder name
    :return: None

    """
    db_filepaths = dict()
    db_folder_location = path.abspath(
        path.join(basepath, "../Patch-Dataset/" + dataset_size + "/" + db_path + "/"))
    obj.db_folder_location = db_folder_location
    db_filepaths['buggy_only'] = path.abspath(
        path.join(db_folder_location, "data.buggy_only"))
    db_filepaths['commit_msg'] = path.abspath(
        path.join(db_folder_location, "data.commit_msg"))
    db_filepaths['fixed_only'] = path.abspath(
        path.join(db_folder_location, "data.fixed_only"))
    db_filepaths['next_code'] = path.abspath(
        path.join(db_folder_location, "data.next_code"))
    db_filepaths['prev_code'] = path.abspath(
        path.join(db_folder_location, "data.prev_code"))
    obj.db_filepaths = db_filepaths

    query_filepaths = dict()
    query_folder_location = path.abspath(
        path.join(basepath, "../Patch-Dataset/" + dataset_size + "/" + query_path + "/"))
    obj.query_folder_location = query_folder_location
    query_filepaths['buggy_only'] = path.abspath(
        path.join(query_folder_location, "data.buggy_only"))
    query_filepaths['commit_msg'] = path.abspath(
        path.join(query_folder_location, "data.commit_msg"))
    query_filepaths['fixed_only'] = path.abspath(
        path.join(query_folder_location, "data.fixed_only"))
    query_filepaths['next_code'] = path.abspath(
        path.join(query_folder_location, "data.next_code"))
    query_filepaths['prev_code'] = path.abspath(
        path.join(query_folder_location, "data.prev_code"))
    obj.query_filepaths = query_filepaths

    return

def get_file_absolute_location(folder_path, filename):
    """
    Return absolute path to load or create file
    :param folder_path: absolute path of directory
    :param filename: filename
    :return: file path
    """
    return path.abspath(
        path.join(folder_path, filename))

class Evaluator:

    """
    Class to compute similarity matrix for each type of evaluator
    Calculate edit distance for top-k results
    """

    def __init__(self, dataset_size, src_lang, tgt_lang, db_data_filename, query_filename, k, concatenate):

        self.dataset_size = dataset_size
        self.src_lang = src_lang
        self.tgt_lang = tgt_lang
        self.db_data_filename = db_data_filename
        self.query_filename = query_filename
        self.k = k
        self.concatenate = concatenate

        self.db_data = None
        self.queries = None
        self.device = None
        self.db_folder_location = None
        self.query_folder_location = None
        self.query_filepaths = None
        self.db_filepaths = None
        self.top_k_similarity_matrix = None

    def set_data(self):

        # Read Data from DB and Queries and set in object
        db_data, queries = [], []
        if self.concatenate:
            # print(evaluator_obj.db_filepaths[db_data_filename[0]])
            for file in self.db_data_filename:
                with open(self.db_filepaths[file], 'r') as f:
                    db_data.append([line.rstrip() for line in f])

            for file in self.query_filename:
                with open(self.query_filepaths[file], 'r') as f:
                    queries.append([line.rstrip() for line in f])

            # db_data[0] = db_data[0][:20]
            # db_data[1] = db_data[1][:20]
            # db_data[1] = db_data[2][:20]
            # queries[0] = queries[0][:10]
            # queries[1] = queries[1][:10]
            # queries[2] = queries[2][:10]

        else:
            with open(self.db_filepaths[self.db_data_filename], 'r') as f:
                db_data = [line.rstrip() for line in f]

            with open(self.query_filepaths[self.query_filename], 'r') as f:
                queries = [line.rstrip() for line in f]

            # db_data = db_data[:20]
            # queries = queries[:10]

        self.db_data = db_data
        self.queries = queries


    def get_top_k_similarity_matrix(self, filename):
        return None

    def calculate_edit_distance(self,
                                top_k_results,
                                edit_distance_filename,
                                norm_edit_distance_filename,
                                visualization_filename,
                                ):
        """
        Compute Normalized Levenshtein distance for top-k results
        Plot distance distribution

        :param top_k_results: torch top-k result
        :param edit_distance_filename: filename to save edit distance
        :param norm_edit_distance_filename: filename to save normalized edit distance
        :param visualization_filename: filename to save distribution plot
        :return: None
        """

        db_fixed_only_filepath = self.db_filepaths['fixed_only']
        query_fixed_only_filepath = self.query_filepaths['fixed_only']

        if type(edit_distance_filename) == str:
            edit_distance_filename = get_file_absolute_location(self.query_folder_location,
                                                                edit_distance_filename
                                                                )
        if type(norm_edit_distance_filename) == str:
            norm_edit_distance_filename = get_file_absolute_location(self.query_folder_location,
                                                                    norm_edit_distance_filename
                                                                    )
        if type(visualization_filename) == str:
            visualization_filename = get_file_absolute_location(self.query_folder_location,
                                                                visualization_filename
                                                                )

        if exists(norm_edit_distance_filename):
            print(norm_edit_distance_filename, ' exists')
            # edit_distances = []
            # f = open(edit_distance_filename, 'r')
            # for line in f:
            #     edit_distances.append(line.strip('\n'))
            # f.close()
            return None
        else:

            if 'train' in self.query_folder_location and self.db_folder_location==self.query_folder_location:
                train_dataset = True
            else:
                train_dataset = False

            with open(db_fixed_only_filepath, 'r') as f:
                db_fixed_only_data = [line.rstrip() for line in f]
            with open(query_fixed_only_filepath, 'r') as f:
                query_fixed_only_data = [line.rstrip() for line in f]

            top_k_indices = top_k_results.indices.data
            top_k_scores = top_k_results.values.data

            edit_distances = []
            norm_edit_distances = []
            # Compute Normalized Levenshtein distance for each query and top-k db results
            for i, _ in enumerate(self.queries if not self.concatenate else self.queries[0]):

                indices = top_k_indices[i]
                scores = top_k_scores[i]
                temp_edit_distances = []
                temp_norm_edit_distances = []
                for index, _ in zip(indices, scores):
                    # if index != i:
                    # print(i, index)
                    if train_dataset and index == i:
                        continue
                    levenshtein_dist = nltk.edit_distance(query_fixed_only_data[i], db_fixed_only_data[index])
                    normalized_levenshtein_dist = levenshtein_dist / (
                        max(len(query_fixed_only_data[i]), len(db_fixed_only_data[index])))
                    temp_edit_distances.append(levenshtein_dist)
                    temp_norm_edit_distances.append(normalized_levenshtein_dist)

                if train_dataset:
                    temp_edit_distances = temp_edit_distances[:self.k-1]
                    temp_norm_edit_distances = temp_norm_edit_distances[:self.k-1]

                edit_distances.append(np.mean(temp_edit_distances))
                norm_edit_distances.append(np.mean(temp_norm_edit_distances))

            with open(edit_distance_filename, 'w') as f:
                for elem in edit_distances:
                    f.write(str(elem) + "\n")

            with open(norm_edit_distance_filename, 'w') as f:
                for elem in norm_edit_distances:
                    f.write(str(elem) + "\n")

        # Plot and save graph
        sns.distplot(norm_edit_distances)
        plt.savefig(visualization_filename)
        plt.clf()

        return None

    def create_retrieved_fixed_dataset(self,
                                       top_k_results,
                                       retrieved_dataset_filename,
                                       ):

        """
        Retrieve fixed code
        :param top_k_results: torch top-k result
        :param retrieved_dataset_filename: Filename
        :return: None
        """
        db_fixed_only_filepath = self.db_filepaths['fixed_only']

        if type(retrieved_dataset_filename) == str:
            retrieved_dataset_filename = get_file_absolute_location(self.query_folder_location,
                                                                    retrieved_dataset_filename)
        if exists(retrieved_dataset_filename):
            print(retrieved_dataset_filename,'exists')
            return None

        # If Query is from train dataset
        if 'train' in self.query_folder_location and self.db_folder_location==self.query_folder_location:
            train_dataset = True
        else:
            train_dataset = False

        with open(db_fixed_only_filepath, 'r') as f:
            db_fixed_only_data = [line.rstrip() for line in f]

        top_k_indices = top_k_results.indices.data
        top_k_scores = top_k_results.values.data

        fixed_code_dataset = []
        # Retrieve fixed code for each query and top-k db results
        for i, _ in enumerate(self.queries if not self.concatenate else self.queries[0]):

            indices = top_k_indices[i]
            scores = top_k_scores[i]
            query_fixed_codes = []
            for index, score in zip(indices, scores):
                # print(i, index, score)
                # Ensure that when query is from train dataset
                # it does not retrieve its own fixed code
                if train_dataset and index == i:
                    continue
                query_fixed_codes.append(db_fixed_only_data[index])
            if train_dataset:
                query_fixed_codes = query_fixed_codes[:self.k-1]

            fixed_code_dataset.append(' <s> '.join(query_fixed_codes))

        with open(retrieved_dataset_filename, 'w') as f:
            for item in fixed_code_dataset:
                f.write("%s\n" % item)

        return None

    def create_filename_with_k(self):
        """
        Filename to save result
        :return: str
        """
        if not self.concatenate:
            return '_' + self.dataset_size \
                   + '_' + str(self.src_lang) \
                   + '_' + str(self.tgt_lang) \
                   + '_' + self.db_data_filename \
                   + '_' + self.query_filename \
                   + '_' + str(self.k)

        else:
            db_data_filename = '_'.join(self.db_data_filename)
            query_filename = '_'.join(self.query_filename)
            return '_' + self.dataset_size \
                   + '_' + str(self.src_lang) \
                   + '_' + str(self.tgt_lang) \
                   + '_' + db_data_filename \
                   + '_' + query_filename \
                   + '_' + str(self.k)

    def create_filename(self):
        """
        Filename to save result
        :return: str
        """
        if not self.concatenate:
            return '_' + self.dataset_size \
                   + '_' + str(self.src_lang) \
                   + '_' + str(self.tgt_lang) \
                   + '_' + self.db_data_filename \
                   + '_' + self.query_filename

        else:
            db_data_filename = '_'.join(self.db_data_filename)
            query_filename = '_'.join(self.query_filename)
            return '_' + self.dataset_size \
                   + '_' + str(self.src_lang) \
                   + '_' + str(self.tgt_lang) \
                   + '_' + db_data_filename \
                   + '_' + query_filename
