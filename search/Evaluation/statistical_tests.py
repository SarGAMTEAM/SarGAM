
from stats_experiments import filenames
from statsmodels.stats.weightstats import ztest as ztest
from scipy.special import rel_entr
from scipy.stats import wasserstein_distance

def dist_diff(file1, file2, test):

    with open(file1,'r') as f:
        dist_1 = [float(line.strip('\n')) for line in f]
    with open(file2, 'r') as f:
        dist_2 = [float(line.strip('\n')) for line in f]

    if test == 'z-test':
        values = ztest(dist_1, dist_2)
        print('Z-test',values)

    elif test =='kl-divergence':
        values = sum(rel_entr(dist_1, dist_2))
        print('KL Divergence',values)

    elif test =='wasserstein distance':
        values = wasserstein_distance(dist_1, dist_2)
        print('Wasserstein distance',values)


if __name__=='__main__':
    for file_pairs in filenames:
        dist_diff(file_pairs[0], file_pairs[1], 'z-test')
        dist_diff(file_pairs[0], file_pairs[1], 'wasserstein distance')


