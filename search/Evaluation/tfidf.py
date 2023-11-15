from sklearn.feature_extraction.text import TfidfVectorizer
from gensim.models import TfidfModel
from gensim.corpora import Dictionary
from sklearn.metrics.pairwise import cosine_similarity
from gensim.matutils import corpus2dense, corpus2csc
from utils import Evaluator, concatenate_data
from os.path import exists

import torch
import numpy as np


class TfIdfEvaluator(Evaluator):

    def __init__(self, dataset_size, src_lang, tgt_lang, db_data_filename, query_filename, k, concatenate):
        super().__init__(dataset_size, src_lang, tgt_lang, db_data_filename, query_filename, k, concatenate)

    def get_top_k_similarity_matrix_sklearn(self,
                                            filename):

        """
        Compute similarity matrix between db and query tfidf vectors
        tfidf vectors calculated using sklearn tfidf vectorizer
        Return top-k results

        :param filename: similarity matrix filename
        :return : torch top-k results
        """
        if exists(filename):
            print(filename + ' exists')
            similarity_matrix = np.load(filename)

        else:
            # Tfidf Vectorizer to fit on db data
            vectorizer = TfidfVectorizer()
            db_data_vectors = vectorizer.fit_transform(
                concatenate_data(self.db_data) if self.concatenate
                else self.db_data
            )
            query_vectors = vectorizer.transform(
                concatenate_data(self.queries) if self.concatenate
                else self.queries
            )

            # Get np array format of tfidf scores
            db_data_matrix = np.array(db_data_vectors.todense())
            query_matrix = np.array(query_vectors.todense())

            # print(db_data_matrix)
            # row = db_data_matrix[10]
            # sns.distplot(row)
            # plt.show()

            similarity_matrix = np.dot(db_data_matrix, query_matrix.T)
            np.save(filename, similarity_matrix)

        # Get top-k results
        similarity_matrix = torch.from_numpy(similarity_matrix)
        top_k_similarity_matrix = torch.topk(similarity_matrix, self.k, dim=1)

        self.top_k_similarity_matrix = top_k_similarity_matrix
        return top_k_similarity_matrix

    def get_top_k_similarity_matrix(self,
                                    filename):

        """
        Compute similarity matrix between db and query tfidf vectors
        tfidf vectors calculated using gensim tfidf vectorizer
        Return top-k results

        :param filename: similarity matrix filename
        :return : torch top-k results
        """
        if exists(filename):
            print(filename + ' exists')
            similarity_matrix = np.load(filename)

        else:
            # Tfidf Vectorizer to fit on db data

            db_corpus, vocab_dict, vocab_size = self.gensim_tfidf_preprocess(
                concatenate_data(self.db_data) if self.concatenate
                else self.db_data
            )
            tfidf_model = TfidfModel(db_corpus)
            db_data_vectors = tfidf_model[db_corpus]
            print('DB TfIdf scores obtained')
            query_corpus, _, _ = self.gensim_tfidf_preprocess(
                concatenate_data(self.queries) if self.concatenate
                else self.queries,
                vocab_dict
            )
            query_vectors = tfidf_model[query_corpus]
            print('Query TfIdf scores obtained')
            # Get np array format of tfidf scores
            db_data_matrix = corpus2dense(db_data_vectors, vocab_size,
                                          len(self.db_data[0]) if self.concatenate else
                                          len(self.db_data)).T
            query_matrix = corpus2dense(query_vectors, vocab_size,
                                        len(self.queries[0]) if self.concatenate else
                                        len(self.queries)).T

            # print(db_data_matrix)
            # row = db_data_matrix[10]
            # sns.distplot(row)
            # plt.show()

            similarity_matrix = cosine_similarity(db_data_matrix, query_matrix).T
            np.save(filename, similarity_matrix)

        # Get top-k results
        similarity_matrix = torch.from_numpy(similarity_matrix)
        top_k_similarity_matrix = torch.topk(similarity_matrix, self.k, dim=1)

        self.top_k_similarity_matrix = top_k_similarity_matrix
        return top_k_similarity_matrix

    def gensim_tfidf_preprocess(self, data, corpus_dict=None):

        doc_tokenized = [doc.split(' ') for doc in data]
        if not corpus_dict:
            corpus_dict = Dictionary(doc_tokenized)
        corpus = [corpus_dict.doc2bow(line) for line in doc_tokenized]

        return corpus, corpus_dict, len(corpus_dict.keys())

    def calculate_edit_distance(self, top_k_similarity_matrix):
        """
        Call Parent class function and pass specific filename

        :param top_k_similarity_matrix: top-k results from get_top_k_similarity_matrix
        :return: None
        """
        edit_distance_filename = 'edit_distances' + self.create_filename_with_k() + '_tfidf'
        norm_edit_distance_filename = 'norm_edit_distances' + self.create_filename_with_k() + '_tfidf'
        visualization_filename = 'visualization' + self.create_filename_with_k() + '_tfidf'
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
        retrieved_dataset_filename = 'retrieved' + self.create_filename_with_k() + '_tfidf'
        super().create_retrieved_fixed_dataset(top_k_similarity_matrix,
                                               retrieved_dataset_filename,
                                               )

        return None
