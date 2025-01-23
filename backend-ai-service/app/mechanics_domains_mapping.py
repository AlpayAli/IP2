import pandas as pd


def extract_unique_values(data, column_name, output_file):
    """
    Extract unique values from a specified column and store them in a file.

    Parameters:
    data (pd.DataFrame): The input dataframe.
    column_name (str): The column to extract unique values from.
    output_file (str): Path to save the unique values to a file.

    Returns:
    None
    """
    # Split the column values into separate lists for each row
    column_split = data[column_name].str.split(', ')

    # Get all unique values from the column
    unique_values = set(value for sublist in column_split.dropna() for value in sublist)

    # Save the unique values to a file
    with open(output_file, 'w') as f:
        for value in sorted(unique_values):
            f.write(f"{value}\n")


def one_hot_encode(data, column_name, unique_values_file):
    """
    Perform one-hot encoding on a specified column using unique values stored in a file.

    Parameters:
    data (pd.DataFrame): The input dataframe.
    column_name (str): The column to be one-hot encoded.
    unique_values_file (str): Path to the file containing unique values.

    Returns:
    pd.DataFrame: The dataframe with one-hot encoded columns added.
    """
    # Read unique values from the file
    with open(unique_values_file, 'r') as f:
        unique_values = [line.strip() for line in f]
    # Split the column values into separate lists for each row
    column_split = data[column_name].str.split(', ')

    # Create one-hot encoded columns for each unique value
    for value in unique_values:
        data[f"{value}"] = column_split.apply(lambda x: 1 if x and value in x else 0)

    return data


def load_mechanic_mapping(file_path):
    """
    Load mechanic-to-supermechanic mapping from a file.

    Parameters:
    file_path (str): Path to the file containing mechanic mappings.

    Returns:
    dict: A dictionary mapping mechanics to supermechanics.
    """
    mechanic_to_superlabel = {}
    with open(file_path, 'r') as f:
        for line in f:
            # Split by tab character instead of colon
            mechanic, supermechanic = line.strip().split('\t')
            mechanic_to_superlabel[mechanic.strip()] = supermechanic.strip()
    return mechanic_to_superlabel


def map_to_supermechanics(data, column_name, mapping_file):
    """
    Map mechanics in a column to their corresponding supermechanics based on a mapping file.

    Parameters:
    data (pd.DataFrame): The input dataframe.
    column_name (str): The column containing mechanics to be mapped.
    mapping_file (str): Path to the file containing mechanic mappings.

    Returns:
    pd.DataFrame: The dataframe with an additional column for supermechanics.
    """
    # Load the mapping from the file
    mechanic_to_superlabel = load_mechanic_mapping(mapping_file)

    # Map mechanics to supermechanics using the dictionary
    data[f"{column_name}_SuperMechanic"] = data[column_name].apply(
        lambda x: ", ".join(set(mechanic_to_superlabel.get(mech.strip(), "Unknown") \
                                for mech in x.split(", "))) if pd.notna(x) else x
    )
    return data


def one_hot_encode_mechanics(data, column_name):
    """
    Perform one-hot encoding on a column containing mechanics.

    Parameters:
    data (pd.DataFrame): The input dataframe.
    column_name (str): The column to be one-hot encoded.

    Returns:
    pd.DataFrame: The dataframe with one-hot encoded columns added.
    """
    # Split the mechanics into separate lists for each row
    mechanic_split = data[column_name].str.split(', ')

    # Get all unique mechanics from the dataset
    unique_mechanics = set(mechanic for sublist in mechanic_split.dropna() for mechanic in sublist)

    # Create one-hot encoded columns for each unique mechanic
    for mechanic in unique_mechanics:
        data[f"{mechanic}"] = mechanic_split.apply(lambda x: 1 if x and mechanic in x else 0)

    return data
