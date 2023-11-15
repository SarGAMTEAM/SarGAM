import pandas as pd


def read_file(filename):
    with open(filename, 'r') as f:
        return [line.rstrip() for line in f]


def concatenate(size, dataset):
    min_val = -1
    max_val = 0.2

    file_location = '../Patch-Dataset/' + size + '/' + dataset + '/'

    # buggy_only = 'data.buggy_only'
    # buggy_fixed = 'retrieved_small_java_java_buggy_only_buggy_only_1_plbart'
    # buggy_norm_edit_dist = 'norm_edit_distances_small_java_java_buggy_only_buggy_only_1_plbart'
    #
    # prev_code = 'data.prev_code'
    # prev_fixed = 'retrieved_small_java_java_prev_code_prev_code_1_plbart'
    # prev_norm_edit_dist = 'norm_edit_distances_small_java_java_prev_code_prev_code_1_plbart'
    #
    # commit_msg = 'data.commit_msg'
    # commit_fixed = 'retrieved_small_java_java_commit_msg_commit_msg_1_plbart'
    # commit_norm_edit_dist = 'norm_edit_distances_small_java_java_commit_msg_commit_msg_1_plbart'

    fixed_code = read_file(file_location + 'data.fixed_only')
    buggy_only = read_file(file_location + 'data.buggy_only')
    buggy_fixed = read_file(file_location + 'retrieved_' + size + '_java_java_buggy_only_buggy_only_1_plbart')
    buggy_norm_edit_dist = read_file(file_location +
                                     'norm_edit_distances_' + size + '_java_java_buggy_only_buggy_only_1_plbart')

    prev_code = read_file(file_location + 'data.prev_code')
    prev_fixed = read_file(file_location + 'retrieved_' + size + '_java_java_prev_code_prev_code_1_plbart')
    prev_norm_edit_dist = read_file(file_location +
                                    'norm_edit_distances_' + size + '_java_java_prev_code_prev_code_1_plbart')

    commit_msg = read_file(file_location + 'data.commit_msg')
    commit_fixed = read_file(file_location + 'retrieved_' + size + '_java_java_commit_msg_commit_msg_1_plbart')
    commit_norm_edit_dist = read_file(file_location +
                                      'norm_edit_distances_' + size + '_java_java_commit_msg_commit_msg_1_plbart')

    data = list(zip(fixed_code,
                    buggy_only, buggy_fixed, buggy_norm_edit_dist,
                    prev_code, prev_fixed, prev_norm_edit_dist,
                    # commit_msg, commit_fixed, commit_norm_edit_dist
                    ))

    df = pd.DataFrame(data,
                      columns=['fixed_code',
                               'buggy_only', 'buggy_fixed', 'buggy_norm_edit_dist',
                               'prev_code', 'prev_fixed', 'prev_norm_edit_dist',
                               # 'commit_msg', 'commit_fixed', 'commit_norm_edit_dist'
                               ]
                      )

    df['buggy_norm_edit_dist'] = pd.to_numeric(df['buggy_norm_edit_dist'])
    df['prev_norm_edit_dist'] = pd.to_numeric(df['prev_norm_edit_dist'])
    # df['commit_norm_edit_dist'] = pd.to_numeric(df['commit_norm_edit_dist'])s

    df = df[(min_val <= df['buggy_norm_edit_dist'])
            & (df['buggy_norm_edit_dist'] <= max_val)
            & (min_val <= df['prev_norm_edit_dist'])
            & (df['prev_norm_edit_dist'] <= max_val)
            ]

    df.to_csv(file_location + 'query_retrieved_score.csv')


concatenate(size='small', dataset='eval')
concatenate(size='small', dataset='test')
#concatenate(size='small', dataset='train')
