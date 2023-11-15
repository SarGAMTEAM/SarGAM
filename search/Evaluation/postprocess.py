def postprocess(add_padding = True, add_lang = True):

    with open(tokens_file_path, 'r') as f:

        tokens = f.readlines()

    max_length = max([len(token) for token in tokens])
    # for i,token in enumerate(tokens):
    #     if len(token) > max_length:
    #         max_length = len(token)
    #         print(i, len(token))
    for i,token in enumerate(tokens):

        if add_padding:

            current_token_length = len(token)
            paddings = " ".join(['<pad>']*(max_length-current_token_length))
            tokens[i] += ' '+ paddings

        if add_lang:
            tokens[i] += ' __java__'

    with open('processesed_tokens', 'w') as f:
        f.writelines(tokens)


if __name__ == '__main__':

    tokens_file_path = '/Users/neelampatodia/Desktop/Yogesh/PatchSearch/Patch-Dataset/tokenized_input'
    postprocess()