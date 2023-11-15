import pandas as pd

def get_dataset():
    file_location = 'Patch-Dataset/small/train/data.'

    def read_file(filename):
        with open(filename, 'r') as f:
            data = f.readlines()
            return data


    commit_msg_filename = file_location + 'commit_msg'
    prev_code_filename = file_location + 'prev_code'
    next_code_filename = file_location + 'next_code'
    buggy_only_filename = file_location + 'buggy_only'
    fixed_only_filename = file_location + 'fixed_only'

    commit_msg = read_file(commit_msg_filename)
    prev_code = read_file(prev_code_filename)
    next_code = read_file(next_code_filename)
    buggy_only = read_file(buggy_only_filename)
    fixed_only = read_file(fixed_only_filename)

    dataset = pd.DataFrame(list(zip(commit_msg,
                                    prev_code,
                                    next_code,
                                    buggy_only,
                                    fixed_only,)),
                           columns = ['commit_msg',
                                      'prev_code',
                                      'next_code',
                                      'buggy_only',
                                      'fixed_only']
                           )

    return dataset.loc[:,['prev_code']]

# d = get_dataset()
# print()

