cd FAIR_SEQ PATH

USER_DIR=SRC_PATH
CUDA_VISIBLE_DEVICES=0 fairseq-train \
    DATA_BIN_PATH
    --user-dir $USER_DIR \
    --save-dir SAVE_PATH
    --ddp-backend=legacy_ddp \
    --task translation_lev_code \
    --criterion nat_loss \
    --arch levenshtein_transformer_code \
    --source-lang src --target-lang tgt --prev-lang prev --mid-lang mid \
    --noise random_delete \
    --share-all-embeddings \
    --optimizer adam --adam-betas '(0.9,0.98)' \
    --lr 0.0005 --lr-scheduler inverse_sqrt \
    --stop-min-lr '1e-09' --warmup-updates 10000 \
    --warmup-init-lr '1e-07' --label-smoothing 0.1 \
    --dropout 0.3 --weight-decay 0.01 \
    --decoder-learned-pos \
    --encoder-learned-pos \
    --apply-bert-init \
    --log-format 'simple' --log-interval 100 \
    --fixed-validation-seed 7 \
    --max-tokens 4000 \
    --max-tokens-valid 2000 \
    --update-freq 4 \
    --skip-invalid-size-inputs-valid-test\
    --max-update 300000 \
    --no-epoch-checkpoints \
    --save-interval-updates 1000 \
    --patience 5 \
    --validate-interval-updates 10 