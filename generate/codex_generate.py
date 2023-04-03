import openai

import os
import time
import openai

from utils import read_prompt

# openai.api_key = os.getenv("OPENAI_API_KEY")
openai.api_key = "kkkkkkkkkkkkkkkk"


def generate_patch_codex(prompt, k=1, temp=0.0, topp=1.0):
    response = openai.Completion.create(
        model="code-davinci-002",
        prompt=prompt,
        max_tokens=50,
        top_p=topp,
        temperature=temp,
        frequency_penalty=0,
        presence_penalty=0,
        n=k
    )
    patches = []
    for p in response['choices']:
        patch = p['text']
        if patch:
            for l in patch.split('\n'):
                if l.strip(' '):
                    patches.append(l.strip(' '))
                    break
        else:
            patch = '////'
    # output = response['choices'][0]['text']
    # patch = output.split('\n')[0].strip(' ')
    # if patch:
    #     pass
    # else:
    #     patch = '//////'
    return patches

def generate_top_k(path, k=1, temperature=0.0, top_p=1):
    prompt = read_prompt(path)
    try:
        patch = generate_patch_codex(prompt, k, temperature, top_p)
    except:
        print('error')

# def generate(dataset, t, patch_t):
#     prompt_dir = f"/proj/arise/arise/cl4062/CodeX/prompts/{t}/{dataset}/"
#     for filename in os.listdir(prompt_dir):
#         f = os.path.join(prompt_dir, filename)
#         # checking if it is a file
#         if os.path.isfile(f):
#             f_name = f.split('/')[-1].split('.')[0]
#             patch_path = f"/proj/arise/arise/cl4062/CodeX/results/{dataset}/{patch_t}/{f_name}.txt"
#             if os.path.isfile(patch_path):
#                 continue

#             prompt = read_prompt(f) ## read a prompt
#             try:
#                 patch = generate_patch_codex(prompt)
        
#                 with open(patch_path, 'w+', encoding='utf-8') as f1:
#                     f1.write(patch)

#                 print(f"{f_name} finished")
#             except:
#                 print(f"{f_name} ERROR!")
#             time.sleep(5)

def generate_k(dataset, t, patch_t, k, p, temp ):
    prompt_dir = f"aaaa/prompts/{t}/{dataset}/"
    patch_root = f"aaaa/results/{dataset}/{patch_t}"
    if not os.path.exists(patch_root):
        os.makedirs(patch_root)
    for filename in os.listdir(prompt_dir):
        f = os.path.join(prompt_dir, filename)
        # checking if it is a file
        if os.path.isfile(f):
            f_name = f.split('/')[-1].split('.')[0]
            patch_path = f"{patch_root}/{f_name}.txt"
            if os.path.isfile(patch_path):
                continue
            prompt = read_prompt(f) ## read a prompt
            try:
                patches = generate_patch_codex(
                    prompt,
                    k=k,
                    temp=temp,
                    topp=p
                    )
        
                with open(patch_path, 'w+', encoding='utf-8') as f1:
                    for patch in patches:
                        f1.write(patch + '\n')
                print(f"{f_name} finished")
            except:
                print(f"{f_name} ERROR!")
            time.sleep(5)

if __name__ == '__main__':
    top_k = 50
    top_p = 0.95
    temperature = 0.8
    dataset = 'defects4j_1.2'
    t = 'left_most_new'
    patch_t = f"K_{str(top_k)}_P_{str(top_p)}_temp_{str(temperature)}"

    generate_k(dataset, t, patch_t, top_k, top_p, temperature)
    generate_k(dataset, t, patch_t, top_k, top_p, temperature)
    generate_k(dataset, t, patch_t, top_k, top_p, temperature)

    # generate('quixbugs', 'left_most_new', 'quixbugs_0.8_0.95')
    # path = '/proj/arise/arise/cl4062/CodeX/prompts/left_most_new/defects4j_1.2/Chart-1.java'
    # generate_top_k(path, 10, 0.8, 0.95)

