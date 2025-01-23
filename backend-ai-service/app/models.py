import json
from langchain.schema import Document


DOCUMENTS_PATH = "data/"
RULES_PATH = "data/texas_holdem_rules.json"

def load_rules():
    """Loads the Texas Hold'em rules from a JSON file."""
    with open(RULES_PATH, "r", encoding="utf-8") as file:
        data = json.load(file)
    return data


def process_rules_to_documents(rules):
    """Converts nested rules JSON into a list of Documents."""
    documents = []

    def recurse(key, value):
        if isinstance(value, dict):
            for sub_key, sub_value in value.items():
                recurse(f"{key}.{sub_key}", sub_value)
        elif isinstance(value, list):
            for idx, item in enumerate(value):
                recurse(f"{key}[{idx}]", item)
        else:
            documents.append(Document(page_content=f"{key}: {value}"))

    recurse("TexasHoldemRules", rules)
    return documents

