import joblib
from os import path
import mechanics_domains_mapping as mapping
import pandas as pd
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class PredictionService:
    def __init__(self):
        base_dir = path.dirname(path.abspath(__file__))
        self.rating_average_model_path = path.join(base_dir, 'ml_models', 'rating_gradient.pkl')
        self.complexity_average_model_path = path.join(base_dir, 'ml_models', 'complexity_voting.pkl')
        self.owned_users_model_path = path.join(base_dir, 'ml_models', 'owned_randomforest.pkl')

        self.unique_domains_file_path = path.join(base_dir, 'data/statistics_data', 'domains_unique.txt')
        self.mechanic_to_superlabel_file_path = path.join(base_dir, 'data/statistics_data',
                                                          'mechanic_to_superlabel.txt')
        self.unique_mechanics_file_path = path.join(base_dir, 'data/statistics_data', 'mechanics_unique.txt')

        self.average_rating_model = self._load_model(self.rating_average_model_path)
        self.complexity_average_model = self._load_model(self.complexity_average_model_path)
        self.owned_users_model = self._load_model(self.owned_users_model_path)

    @staticmethod
    def _load_model(model_path):
        if not path.exists(model_path):
            logger.error(f"Model file not found at path: {model_path}")
            raise FileNotFoundError(f"Model file not found: {model_path}")
        return joblib.load(model_path)

    def prepare_input_data(self, request_data):
        min_players = request_data['min_players']
        max_players = request_data['max_players']
        play_time = request_data['play_time']
        min_age = request_data['min_age']
        users_rated = request_data['users_rated']
        domains_combined = ', '.join(request_data['domains'])
        mechanics_combined = ', '.join(request_data['mechanics'])
        domains = pd.DataFrame({'domains': [domains_combined]})
        mechanics = pd.DataFrame({'mechanics': [mechanics_combined]})
        self.one_hot_encode_domains(domains)
        self.one_hot_encode_mechanics(mechanics)
        base_df = pd.DataFrame({
            'min_players': [min_players],
            'max_players': [max_players],
            'play_time': [play_time],
            'min_age': [min_age],
            'users_rated': [users_rated]
        })
        data = pd.concat([base_df, domains, mechanics], axis=1)
        data.drop(columns=['domains', 'mechanics', 'mechanics_SuperMechanic'], inplace=True)
        input_data = data
        input_data = input_data.rename(columns={
            "min_players": "Min Players",
            "max_players": "Max Players",
            "play_time": "Play Time",
            "min_age": "Min Age",
            "users_rated": "Users Rated",
        })
        return input_data

    def predict_average_rating(self, data):
        input_data = self.prepare_input_data(data)
        prediction = self.average_rating_model.predict(input_data)[0]
        return prediction

    def predict_complexity_average(self, data):
        input_data = self.prepare_input_data(data)
        prediction = self.complexity_average_model.predict(input_data)[0]
        return prediction

    def predict_owned_users(self, data):
        input_data = self.prepare_input_data(data)
        prediction = self.owned_users_model.predict(input_data)[0]
        return prediction

    def one_hot_encode_domains(self, data):
        encoded_domains = mapping.one_hot_encode(data, column_name='domains',
                                                 unique_values_file=self.unique_domains_file_path)
        return encoded_domains

    def one_hot_encode_mechanics(self, data):
        mechanics = mapping.map_to_supermechanics(data, column_name='mechanics',
                                                  mapping_file=self.mechanic_to_superlabel_file_path)
        one_hot_encoded_mechanics = mapping.one_hot_encode(mechanics, column_name='mechanics_SuperMechanic',
                                                           unique_values_file=self.unique_mechanics_file_path)
        return one_hot_encoded_mechanics
