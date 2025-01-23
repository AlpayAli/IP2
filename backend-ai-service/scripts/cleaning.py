import numpy as np
import pandas as pd
import re


def clean_rating(rating_column:pd.Series):
    cleaned_rows = []
    for index, row in rating_column.items():
        if isinstance(row, str):
            cleaned_row = row.replace('*','').strip()
            cleaned_rows.append(float(cleaned_row))
        else:
            cleaned_rows.append(row)
    return pd.DataFrame(cleaned_rows)

def clean_play_time(play_time_column:pd.Series):
    cleaned_rows = []

    word_to_num = {
        'one': 1, 'two': 2, 'three': 3, 'four': 4,
        'five': 5, 'six': 6, 'seven': 7, 'eight': 8,
        'nine': 9, 'ten': 10, 'eleven': 11, 'twelve': 12
    }
    patterns = [
        (r'~?(\d+)\s*hours?', lambda m: str(int(m.group(1)) * 60)),
        (r'~?(one|two|three|four|five|six|seven|eight|nine|ten|eleven|twelve)\s*hours?',
         lambda m: str(word_to_num[m.group(1).lower()] * 60)),
        (r'(\d+)\s*min', lambda m: m.group(1))
    ]

    for index, row in play_time_column.items():
        if isinstance(row, str):
            for pattern, repl in patterns:
                result = re.sub(pattern, repl, row, flags=re.IGNORECASE)
                if result != row:
                    cleaned_rows.append(float(result))
        else:
            cleaned_rows.append(row)
    return pd.DataFrame(cleaned_rows)

def clean_year_published(published_column:pd.Series):
    cleaned_rows = []
    year_pattern = r'^\d{4}$'

    for index, row in published_column.items():
        if isinstance(row, str):
            if re.match(year_pattern, row.strip()):
                cleaned_rows.append(int(row.strip()))
            else:
                cleaned_rows.append(np.nan)
        else:
            cleaned_rows.append(row)
    return pd.DataFrame(cleaned_rows, columns=["Year"])

def clean_player_count(df: pd.DataFrame, min_col: str, max_col: str):
    """
    Cleans the Min Players and Max Players columns of a DataFrame.

    Args:
        df (pd.DataFrame): The DataFrame containing player count columns.
        min_col (str): The column name for Min Players.
        max_col (str): The column name for Max Players.

    Returns:
        pd.DataFrame: The DataFrame with cleaned Min Players and Max Players columns.
    """

    for idx, value in df[min_col].items():
        if pd.isna(value) or value < 1:
            df.at[idx, min_col] = 1

    for idx, value in df[max_col].items():
        min_value = df.at[idx, min_col]
        if pd.isna(value) or value < min_value:
            df.at[idx, max_col] = min_value + 1
    return df


def clean_min_age(age_column:pd.Series):
    cleaned_rows = []
    for index, row in age_column.items():
        if row>25:
            cleaned_rows.append(25)
        else:
            cleaned_rows.append(row)

    return pd.DataFrame(cleaned_rows)


