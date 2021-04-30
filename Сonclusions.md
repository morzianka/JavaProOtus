| Name       | Full GC         | Concurrent | Remark     | Cleanup    | Avg Stop the World | Total Stop the World | Total time  | 
| :--------: | :-------------: | :--------: | :--------: | :--------: | :----------------: | :------------------: | :---------: |
| G1GC       | 8 sec 199 ms    | 848 ms     | 10.8 ms    | 1.19 ms    | 42.7 ms            | 8 sec 459 ms         | 76.837 s    |
| ParallelGC | 1 min 994 ms    |     -      |      -     |     -      | 93.8 ms            | 1 min 994 ms         | 246.658 s   |
| SerialGC   | 5 sec 332 ms    |      -     |      -     |       -    | 211 ms             | 5 sec 703 ms         | 187.467 s   |

На малом размере кучи и 1-ом ядре подходит SerialGC.
ParallelGC показал себя хуже всех, либо его лог надо анализировать иначе.

По умолчанию система выбрала G1, размер памяти изменялся динамически от 256 до 2048 и выше.
На большом обёме показатели G1 выросли при соотношении кол-ва Young GC сборок 87 к 36.

| Name       | Full GC         | Concurrent   | Remark     | Cleanup    | Avg Stop the World | Total Stop the World |
| :--------: | :-------------: | :----------: | :--------: | :--------: | :----------------: | :------------------: |
| G1GC ~512  | 8 sec 199 ms    | 848 ms       | 10.8 ms    | 1.19 ms    | 42.7 ms            | 8 sec 459 ms         |
| G1GC ~2048 | 3 sec 53 ms     | 2 sec 590 ms | 21.8 ms    | 2.28 ms    | 9.84 ms            | 463 ms               |
