from plot_experiments import filenames
import matplotlib.pyplot as plt
from os import path

def plot_edit_dist_(files):

    min_val = -1
    max_val = 1.1
    file_1 = files[0]
    file_2 = files[1]
    x, y = [], []
    # with open(file_1) as f:
    #     line = float(f.readline().strip('\n'))
    #     y.append(line)
    # with open(file_1) as f:
    #     line = float(f.readline().strip('\n'))
    #     x.append(line)
    f = open(file_1, 'r')
    for line in f:
        y.append(float(line.strip('\n')))
    f.close()
    f = open(file_2, 'r')
    for line in f:
        x.append(float(line.strip('\n')))
    f.close()



    data_pair = zip(x, y)

    cut_data_pair = []

    for a,b in data_pair:

        if a < min_val or a > max_val or b < min_val or b > max_val:
            continue
        cut_data_pair.append((a,b))

    data_pair = cut_data_pair
    if 'prev' in file_1:
        y_label = 'Fixed Code Normalized Edit Distance Retrieved using Prev Code'
        y_title = 'Prev Code'
    elif 'commit' in file_1:
        y_label = 'Fixed Code Normalized Edit Distance Retrieved using Commit Msg'
        y_title = 'Commit Message'
    else:
        y_label = 'Fixed Code Normalized Edit Distance Retrieved using Buggy Only'
        y_title = 'Buggy Only'

    if 'prev' in file_2:
        x_label = 'Fixed Code Normalized Edit Distance Retrieved using Prev Code'
        x_title = 'Prev Code'
    elif 'commit' in file_2:
        x_label = 'Fixed Code Normalized Edit Distance Retrieved using Commit Msg'
        x_title = 'Commit Message'
    else:
        x_label = 'Fixed Code Normalized Edit Distance Retrieved using Buggy Only'
        x_title = 'Buggy Only'

    plt.scatter(*zip(*data_pair), s=1)
    #plt.subplots_adjust(bottom=0.6)
    plt.xlabel(x_label)
    plt.ylabel(y_label)
    plt.title(y_title + ' vs ' + x_title)
    viz_filename = file_1.split('/')[-1] + '_' + file_2.split('/')[-1]
    viz_filepath = '/'.join(file_1.split('/')[:-1]) + '/' + viz_filename
    #viz_abspath = path.abspath(vi)
    plt.savefig(viz_filepath)
    plt.clf()

for pair in filenames:
    plot_edit_dist_(pair)
