Developed by Changshu Liu, Pelin Cetin, Yogesh Patodia under the leadership of Prof Baishakhi Ray, Dr. Saikat Chakraborty and Yangruibo Ding.

You can read the paper here: https://arxiv.org/abs/2306.06490 . This work has been accepted to IEEE Transactions on Software Engineering, 2024 and will be presented at International Conference of Software Engineering, 2024.

## Background

Code editing is essential in evolving software development. In literature, several automated code editing tools are proposed, which leverage Information Retrieval-based techniques and Machine Learning-based code generation and code editing
models. Each technique comes with its own promises and perils, and for this reason, they are often used together to complement their strengths and compensate for their weaknesses. This paper proposes a hybrid approach to better synthesize code edits by
leveraging the power of code search, generation, and modification. Our key observation is that a patch that is obtained by search & retrieval, even if incorrect, can provide helpful guidance to a code generation model. However, a retrieval-guided patch produced 
by a code generation model can still be a few tokens off from the intended patch. Such generated patches can be slightly modified to create the intended patches. We developed a novel tool to solve this challenge: SARGAM, which is designed to follow a real developer’s
code editing behavior. Given an original code version, the developer may search for the related patches, generate or write the code, and then modify the generated code to adapt it to the right context. Our evaluation of SARGAM on edit generation shows superior performance
w.r.t. the current state-of-the-art techniques. SARGAM also shows its effectiveness on automated program repair tasks.
