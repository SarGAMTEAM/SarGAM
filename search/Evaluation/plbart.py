from utils import Evaluator, concatenate_data, get_file_absolute_location
from modit import load_modit_model
from transformers import PLBartTokenizer, PLBartModel
from os.path import exists
from sklearn.metrics.pairwise import cosine_similarity
import torch
import numpy as np


class PlBartEvaluator(Evaluator):

    def __init__(self, dataset_size, src_lang, tgt_lang, db_data_filename, query_filename, k, concatenate,
                 model_ckpt=None):

        super().__init__(dataset_size, src_lang, tgt_lang, db_data_filename, query_filename, k, concatenate)
        self.device = "cuda:0" if torch.cuda.is_available() else "cpu"
        if model_ckpt is None:
            self.model_name = 'plbart'
        elif 'modit' in model_ckpt:
            self.model_name = 'modit'
        self.tokenizer, self.model = self.load_model_and_tokenizer(model_ckpt=model_ckpt)
        self.tokenizer_max_length = 1024

    def set_data(self):
        """
        Call parent function to read from files and set in object
        Truncate length of each sentence to max tokenizer length
        :return: None
        """
        super().set_data()
        if self.concatenate:
            n_data = len(self.db_data)
            for i, db_data in enumerate(self.db_data):
                # this makes sure that all the modalities passed to the model
                # are of the same length (e.g. for prev_code and buggy_only for
                # max_size 1024, both will be of length 512)
                self.db_data[i] = [data[:(self.tokenizer_max_length//n_data-1)]
                                   for data in db_data]
            for i, query_data in enumerate(self.queries):
                self.queries[i] = [data[:(self.tokenizer_max_length//n_data-1)]
                                   for data in query_data]

        else:
            self.db_data = [data[:self.tokenizer_max_length]
                            for data in self.db_data]
            self.queries = [data[:self.tokenizer_max_length]
                            for data in self.queries]

    def load_model_and_tokenizer(self,
                                 tf_save_directory="uclanlp/plbart-base",
                                 model_ckpt=None
                                 ):
        tokenizer = PLBartTokenizer.from_pretrained(tf_save_directory,
                                                    src_lang=self.src_lang,
                                                    tgt_lang=self.tgt_lang,
                                                    )
        if model_ckpt is None:
            model = PLBartModel.from_pretrained('uclanlp/plbart-base').to(self.device)
        else:
            if self.model_name == 'modit':
                model = load_modit_model(model_ckpt)
                model = model.to(self.device)
            else:
                model = PLBartModel.from_pretrained(model_ckpt).to(self.device)
        model.eval()

        return tokenizer, model

    def create_load_embeddings(self,
                               train_set,
                               embeddings_filename
                               ):
        """
        Function to create or load data embeddings

        :param train_set: DB or Query file content
        :param embeddings_filename: Filename to save embeddings
        :return: list of embeddinds of dimension (samples, model_last_layer_dimension)
        """
        if exists(embeddings_filename):
            print(embeddings_filename + ' exists')
            embeddings = np.load(embeddings_filename)
            return embeddings

        embeddings = []
        for j in range(len(train_set)):
            code_encodings = self.tokenizer(train_set[j],
                                            return_tensors="pt").to(self.device)
            with torch.no_grad():
                code_embeddings = self.model(**code_encodings)
            code_embeddings = torch.mean(code_embeddings.last_hidden_state, dim=1)
            code_embeddings = code_embeddings.flatten()

            embeddings.append(code_embeddings.detach().cpu().numpy())

        embeddings = np.array(embeddings)
        np.save(embeddings_filename, embeddings)
        return embeddings

    def get_top_k_similarity_matrix(self,
                                    filename,
                                    ):
        """
        Compute similarity matrix between db and query tfidf vectors
        Return top-k results

        :param filename: similarity matrix filename
        :return : torch top-k results
        """
        if exists(filename):
            print(filename + ' exists')
            similarity_matrix = np.load(filename)
        else:
            # Get PlBart DB and Query Embeddings
            embeddings_filename = 'embeddings' + self.create_filename() + f'_{self.model_name}.npy'
            embeddings_filename = get_file_absolute_location(self.db_folder_location,
                                                             embeddings_filename)
            db_data_embeddings = self.create_load_embeddings(
                concatenate_data(self.db_data) if self.concatenate
                else self.db_data,
                embeddings_filename
            )

            embeddings_filename = 'embeddings' + self.create_filename() + f'_{self.model_name}.npy'
            embeddings_filename = get_file_absolute_location(self.query_folder_location,
                                                             embeddings_filename)
            query_embeddings = self.create_load_embeddings(
                concatenate_data(self.queries) if self.concatenate
                else self.queries,
                embeddings_filename
            )

            similarity_matrix = cosine_similarity(db_data_embeddings, query_embeddings).T
            np.save(filename, similarity_matrix)

        # Get top-k results
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
        edit_distance_filename = 'edit_distances' + self.create_filename_with_k() + f'_{self.model_name}'
        norm_edit_distance_filename = 'norm_edit_distances' + self.create_filename_with_k() + f'_{self.model_name}'
        visualization_filename = 'visualization' + self.create_filename_with_k() + f'_{self.model_name}'
        super().calculate_edit_distance(top_k_similarity_matrix,
                                        edit_distance_filename,
                                        norm_edit_distance_filename,
                                        visualization_filename)

        # Delete model and tokenizer to clear memory
        # del self.tokenizer
        # del self.model

        return None

    def create_retrieved_fixed_dataset(self,
                                       top_k_similarity_matrix,
                                       ):

        """
        Call Parent class function and pass specific filename

        :param top_k_similarity_matrix: top-k results from get_top_k_similarity_matrix
        :return: None
        """
        retrieved_dataset_filename = 'retrieved' + self.create_filename_with_k() + f'_{self.model_name}'
        super().create_retrieved_fixed_dataset(top_k_similarity_matrix,
                                               retrieved_dataset_filename,
                                               )

        # Delete model and tokenizer to clear memory
        # del self.tokenizer
        # del self.model

        return None
