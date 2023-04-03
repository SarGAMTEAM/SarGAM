from utils import read_prompt
from codex_generate import generate_patch_codex
import time
from tqdm import tqdm
import os

def read_bug_d4j1():
    bug_ids = []
    path = 'XXXXX/data/Defects4J_oneLiner_metadata.csv'
    with open(path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        for line in lines:
            b = line.split(',')[0]
            n = line.split(',')[1]
            bug_ids.append(f"{b}-{n}")
    return bug_ids

def read_bug_d4j2():
    bug_ids = []
    path = 'XXXXX/d4j_2.0/data/bug_ids.txt'
    with open(path, 'r', encoding='utf-8') as f:
        for line in f.readlines():
            line = line.strip('\n').strip()
            bug_ids.append(line)
    return bug_ids

def read_retrv_d4j1():
    lines = []
    path = 'xxxx/retrieved_small_java_java_buggy_only_buggy_only_25_plbart'
    with open(path, 'r', encoding='utf-8') as f:
        for line in f.readlines():
            line=line.strip('\n')
            lines.append(line)
    return lines

def read_retrv_d4j2():
    lines = []
    path = 'YYYYYY/retrieved_small_java_java_buggy_only_buggy_only_25_plbart'
    with open(path, 'r', encoding='utf-8') as f:
        for line in f.readlines():
            line=line.strip('\n')
            lines.append(line)
    return lines

def merge_retrv(new_line, prompt):
    prompt_lines = prompt.split('\n')
    i = 0
    for line in prompt_lines:
        if 'buggy line is here' in line:
            break
        i += 1
    prompt_lines.insert(i+1, new_line)
    new_prompt = '\n'.join(prompt_lines)
    return new_prompt

def run_all(topk):
    top_k = topk
    top_p = 0.95
    temperature = 0.8

    bugs = read_bug_d4j1()
    retrv_patches = read_retrv_d4j1()
    prompts = []
    root = 'ZZZZZ/defects4j_2.0/'
    
    for bug in bugs:
        prompt_path = root + bug + '.java'
        p = read_prompt(prompt_path)
        prompts.append(p)
    
    assert len(bugs) == len(retrv_patches)
    assert len(retrv_patches) == len(prompts)

    root2write = f'LLLLLL/retrieved_patches_all_{top_k}/'
    if not os.path.exists(root2write):
        os.mkdir(root2write)

    for bug, patches, prompt in zip(bugs, retrv_patches, prompts):
        file_path = root2write + bug + '.txt'
        if os.path.exists(file_path):
            continue
        f = open(file_path, 'w', encoding='utf-8')
        patch_hitory = []
        for patch in tqdm(patches.split('<s>')):
            patch = patch.strip()
            if not patch:
                continue
            if patch in patch_hitory:
                continue
            patch_hitory.append(patch)
            new_line = f"/// a possible patch for the buggy line: {patch}"
            new_prompt = merge_retrv(new_line, prompt)
            logger = 0   
            while True:
                if logger > 100:
                    print(f"{bug} exits")
                    break
                try:
                    ps = generate_patch_codex(
                        new_prompt,
                        top_k,
                        temperature,
                        top_p
                    )
                    if ps:
                        break
                except:
                    pass
                logger += 1
                time.sleep(1)
            for i in ps:
                f.write(i +'\n')
        print(f'{bug} finished')


if __name__ == "__main__":
    # run_all(1)
    # run_all(10)
    run_all(100)

