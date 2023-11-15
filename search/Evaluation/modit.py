from transformers import PLBartModel, AutoConfig
import torch

def load_modit_model(model_path=None):
    if model_path is None:
        model_path = '../modit_checkpoints/small.parent_contexed_commit.child_code/checkpoint_best.pt'
    state_dict = torch.load(model_path)["model"]
    config = AutoConfig.from_pretrained("uclanlp/plbart-base")
    model = PLBartModel(config)

    model = PLBartModel._load_state_dict_into_model(
        model,
        state_dict,
        model_path
    )
    return model[0]
