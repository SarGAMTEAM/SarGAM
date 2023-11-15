# filenames = [
#
#     ['../Patch-Dataset/small/train/norm_edit_distances_small_java_java_buggy_only_buggy_only_2_plbart',
#      '../Patch-Dataset/small/train/norm_edit_distances_small_java_java_prev_code_prev_code_2_plbart']
# ]
filenames = []
ks = [1]#, 2, 3]
dataset_size = ['small',]#'medium']#, 'medium']
query = ['train', 'eval', 'test']

for k in ks:
    for size in dataset_size:
        for q in query:
            new_k = str(k + 1) if q == 'train' else str(k)

            filename1 = '../Patch-Dataset/' + size + '/' + q + '/norm_edit_distances_' \
                        + size + '_java_java_commit_msg_commit_msg_' + new_k + '_plbart'
            filename2 = '../Patch-Dataset/' + size + '/' + q + '/norm_edit_distances_' \
                        + size + '_java_java_prev_code_prev_code_' + new_k + '_plbart'
            filenames.append([filename2, filename1])

            filename1 = '../Patch-Dataset/' + size + '/' + q + '/norm_edit_distances_' \
                        + size + '_java_java_buggy_only_buggy_only_' + new_k + '_plbart'
            filename2 = '../Patch-Dataset/' + size + '/' + q + '/norm_edit_distances_' \
                        + size + '_java_java_prev_code_prev_code_' + new_k + '_plbart'
            filenames.append([filename1, filename2])

            filename1 = '../Patch-Dataset/' + size + '/' + q + '/norm_edit_distances_' \
                        + size + '_java_java_buggy_only_buggy_only_' + new_k + '_plbart'
            filename2 = '../Patch-Dataset/' + size + '/' + q + '/norm_edit_distances_' \
                        + size + '_java_java_commit_msg_commit_msg_' + new_k + '_plbart'
            filenames.append([filename1, filename2])

