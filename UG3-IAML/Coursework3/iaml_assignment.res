=== Run information ===

Scheme:weka.classifiers.meta.FilteredClassifier -F "weka.filters.unsupervised.attribute.AddExpression -E \"0.1  +  a23 * 0.2  + a88 * 0.13 + a92 * -0.11 + a97 * -0.1 + a123 * 0.08 + a130 * -0.09 + a171 * -0.05 + a206 * 0.06 + a311 * 0.1  + a387 * -0.05\" -N expression" -W weka.classifiers.functions.Logistic -- -R 1.0E-8 -M -1
Relation:     all_train-weka.filters.unsupervised.attribute.Remove-R1-weka.filters.unsupervised.attribute.AddExpression-Ea501 * 1.78 + a502 * -0.32 + a503 * 1.76 + a504 * 0.8  + a506 * 0.4  + a507 * 0.56 + a508 * 1.52 + a509 * 0.2  + a510 * 0.95 + a512 * 0.86 + a516 * 0.42 + a517 * 0.84 + a519 * 0.44-Nexpression-weka.filters.unsupervised.attribute.Remove-R520-weka.filters.unsupervised.attribute.AddExpression-Ea501 * 1.78 + a502 * -0.32 + a503 * 1.76 + a504 * 0.8  + a506 * 0.4  + a507 * 0.56 + a508 * 1.52 + a509 * 0.2  + a510 * 0.95 + a512 * 0.86 + a516 * 0.42 + a517 * 0.84 + a519 * 0.44-Nexpression-weka.filters.AllFilter-weka.filters.MultiFilter-Fweka.filters.unsupervised.attribute.AddExpression -E "a501 * 1.78 + a502 * -0.32 + a503 * 1.76 + a504 * 0.8  + a506 * 0.4  + a507 * 0.56 + a508 * 1.52 + a509 * 0.2  + a510 * 0.95 + a512 * 0.86 + a516 * 0.42 + a517 * 0.84 + a519 * 0.44" -N expression-Fweka.filters.AllFilter-weka.filters.unsupervised.attribute.Remove-R501-514,516-520
Instances:    2113
Attributes:   501
[list of attributes omitted]
Test mode:split 80.0% train, remainder test

=== Classifier model (full training set) ===

FilteredClassifier using weka.classifiers.functions.Logistic -R 1.0E-8 -M -1 on data filtered through weka.filters.unsupervised.attribute.AddExpression -E "0.1  +  a23 * 0.2  + a88 * 0.13 + a92 * -0.11 + a97 * -0.1 + a123 * 0.08 + a130 * -0.09 + a171 * -0.05 + a206 * 0.06 + a311 * 0.1  + a387 * -0.05" -N expression

Filtered Header
@relation 'all_train-weka.filters.unsupervised.attribute.Remove-R1-weka.filters.unsupervised.attribute.AddExpression-Ea501 * 1.78 + a502 * -0.32 + a503 * 1.76 + a504 * 0.8  + a506 * 0.4  + a507 * 0.56 + a508 * 1.52 + a509 * 0.2  + a510 * 0.95 + a512 * 0.86 + a516 * 0.42 + a517 * 0.84 + a519 * 0.44-Nexpression-weka.filters.unsupervised.attribute.Remove-R520-weka.filters.unsupervised.attribute.AddExpression-Ea501 * 1.78 + a502 * -0.32 + a503 * 1.76 + a504 * 0.8  + a506 * 0.4  + a507 * 0.56 + a508 * 1.52 + a509 * 0.2  + a510 * 0.95 + a512 * 0.86 + a516 * 0.42 + a517 * 0.84 + a519 * 0.44-Nexpression-weka.filters.AllFilter-weka.filters.MultiFilter-Fweka.filters.unsupervised.attribute.AddExpression -E \"a501 * 1.78 + a502 * -0.32 + a503 * 1.76 + a504 * 0.8  + a506 * 0.4  + a507 * 0.56 + a508 * 1.52 + a509 * 0.2  + a510 * 0.95 + a512 * 0.86 + a516 * 0.42 + a517 * 0.84 + a519 * 0.44\" -N expression-Fweka.filters.AllFilter-weka.filters.unsupervised.attribute.Remove-R501-514,516-520-weka.filters.unsupervised.attribute.AddExpression-E0.1  +  a23 * 0.2  + a88 * 0.13 + a92 * -0.11 + a97 * -0.1 + a123 * 0.08 + a130 * -0.09 + a171 * -0.05 + a206 * 0.06 + a311 * 0.1  + a387 * -0.05-Nexpression'

@attribute dim1 numeric
@attribute dim2 numeric
@attribute dim3 numeric
@attribute dim4 numeric
@attribute dim5 numeric
@attribute dim6 numeric
@attribute dim7 numeric
@attribute dim8 numeric
@attribute dim9 numeric
@attribute dim10 numeric
@attribute dim11 numeric
@attribute dim12 numeric
@attribute dim13 numeric
@attribute dim14 numeric
@attribute dim15 numeric
@attribute dim16 numeric
@attribute dim17 numeric
@attribute dim18 numeric
@attribute dim19 numeric
@attribute dim20 numeric
@attribute dim21 numeric
@attribute dim22 numeric
@attribute dim23 numeric
@attribute dim24 numeric
@attribute dim25 numeric
@attribute dim26 numeric
@attribute dim27 numeric
@attribute dim28 numeric
@attribute dim29 numeric
@attribute dim30 numeric
@attribute dim31 numeric
@attribute dim32 numeric
@attribute dim33 numeric
@attribute dim34 numeric
@attribute dim35 numeric
@attribute dim36 numeric
@attribute dim37 numeric
@attribute dim38 numeric
@attribute dim39 numeric
@attribute dim40 numeric
@attribute dim41 numeric
@attribute dim42 numeric
@attribute dim43 numeric
@attribute dim44 numeric
@attribute dim45 numeric
@attribute dim46 numeric
@attribute dim47 numeric
@attribute dim48 numeric
@attribute dim49 numeric
@attribute dim50 numeric
@attribute dim51 numeric
@attribute dim52 numeric
@attribute dim53 numeric
@attribute dim54 numeric
@attribute dim55 numeric
@attribute dim56 numeric
@attribute dim57 numeric
@attribute dim58 numeric
@attribute dim59 numeric
@attribute dim60 numeric
@attribute dim61 numeric
@attribute dim62 numeric
@attribute dim63 numeric
@attribute dim64 numeric
@attribute dim65 numeric
@attribute dim66 numeric
@attribute dim67 numeric
@attribute dim68 numeric
@attribute dim69 numeric
@attribute dim70 numeric
@attribute dim71 numeric
@attribute dim72 numeric
@attribute dim73 numeric
@attribute dim74 numeric
@attribute dim75 numeric
@attribute dim76 numeric
@attribute dim77 numeric
@attribute dim78 numeric
@attribute dim79 numeric
@attribute dim80 numeric
@attribute dim81 numeric
@attribute dim82 numeric
@attribute dim83 numeric
@attribute dim84 numeric
@attribute dim85 numeric
@attribute dim86 numeric
@attribute dim87 numeric
@attribute dim88 numeric
@attribute dim89 numeric
@attribute dim90 numeric
@attribute dim91 numeric
@attribute dim92 numeric
@attribute dim93 numeric
@attribute dim94 numeric
@attribute dim95 numeric
@attribute dim96 numeric
@attribute dim97 numeric
@attribute dim98 numeric
@attribute dim99 numeric
@attribute dim100 numeric
@attribute dim101 numeric
@attribute dim102 numeric
@attribute dim103 numeric
@attribute dim104 numeric
@attribute dim105 numeric
@attribute dim106 numeric
@attribute dim107 numeric
@attribute dim108 numeric
@attribute dim109 numeric
@attribute dim110 numeric
@attribute dim111 numeric
@attribute dim112 numeric
@attribute dim113 numeric
@attribute dim114 numeric
@attribute dim115 numeric
@attribute dim116 numeric
@attribute dim117 numeric
@attribute dim118 numeric
@attribute dim119 numeric
@attribute dim120 numeric
@attribute dim121 numeric
@attribute dim122 numeric
@attribute dim123 numeric
@attribute dim124 numeric
@attribute dim125 numeric
@attribute dim126 numeric
@attribute dim127 numeric
@attribute dim128 numeric
@attribute dim129 numeric
@attribute dim130 numeric
@attribute dim131 numeric
@attribute dim132 numeric
@attribute dim133 numeric
@attribute dim134 numeric
@attribute dim135 numeric
@attribute dim136 numeric
@attribute dim137 numeric
@attribute dim138 numeric
@attribute dim139 numeric
@attribute dim140 numeric
@attribute dim141 numeric
@attribute dim142 numeric
@attribute dim143 numeric
@attribute dim144 numeric
@attribute dim145 numeric
@attribute dim146 numeric
@attribute dim147 numeric
@attribute dim148 numeric
@attribute dim149 numeric
@attribute dim150 numeric
@attribute dim151 numeric
@attribute dim152 numeric
@attribute dim153 numeric
@attribute dim154 numeric
@attribute dim155 numeric
@attribute dim156 numeric
@attribute dim157 numeric
@attribute dim158 numeric
@attribute dim159 numeric
@attribute dim160 numeric
@attribute dim161 numeric
@attribute dim162 numeric
@attribute dim163 numeric
@attribute dim164 numeric
@attribute dim165 numeric
@attribute dim166 numeric
@attribute dim167 numeric
@attribute dim168 numeric
@attribute dim169 numeric
@attribute dim170 numeric
@attribute dim171 numeric
@attribute dim172 numeric
@attribute dim173 numeric
@attribute dim174 numeric
@attribute dim175 numeric
@attribute dim176 numeric
@attribute dim177 numeric
@attribute dim178 numeric
@attribute dim179 numeric
@attribute dim180 numeric
@attribute dim181 numeric
@attribute dim182 numeric
@attribute dim183 numeric
@attribute dim184 numeric
@attribute dim185 numeric
@attribute dim186 numeric
@attribute dim187 numeric
@attribute dim188 numeric
@attribute dim189 numeric
@attribute dim190 numeric
@attribute dim191 numeric
@attribute dim192 numeric
@attribute dim193 numeric
@attribute dim194 numeric
@attribute dim195 numeric
@attribute dim196 numeric
@attribute dim197 numeric
@attribute dim198 numeric
@attribute dim199 numeric
@attribute dim200 numeric
@attribute dim201 numeric
@attribute dim202 numeric
@attribute dim203 numeric
@attribute dim204 numeric
@attribute dim205 numeric
@attribute dim206 numeric
@attribute dim207 numeric
@attribute dim208 numeric
@attribute dim209 numeric
@attribute dim210 numeric
@attribute dim211 numeric
@attribute dim212 numeric
@attribute dim213 numeric
@attribute dim214 numeric
@attribute dim215 numeric
@attribute dim216 numeric
@attribute dim217 numeric
@attribute dim218 numeric
@attribute dim219 numeric
@attribute dim220 numeric
@attribute dim221 numeric
@attribute dim222 numeric
@attribute dim223 numeric
@attribute dim224 numeric
@attribute dim225 numeric
@attribute dim226 numeric
@attribute dim227 numeric
@attribute dim228 numeric
@attribute dim229 numeric
@attribute dim230 numeric
@attribute dim231 numeric
@attribute dim232 numeric
@attribute dim233 numeric
@attribute dim234 numeric
@attribute dim235 numeric
@attribute dim236 numeric
@attribute dim237 numeric
@attribute dim238 numeric
@attribute dim239 numeric
@attribute dim240 numeric
@attribute dim241 numeric
@attribute dim242 numeric
@attribute dim243 numeric
@attribute dim244 numeric
@attribute dim245 numeric
@attribute dim246 numeric
@attribute dim247 numeric
@attribute dim248 numeric
@attribute dim249 numeric
@attribute dim250 numeric
@attribute dim251 numeric
@attribute dim252 numeric
@attribute dim253 numeric
@attribute dim254 numeric
@attribute dim255 numeric
@attribute dim256 numeric
@attribute dim257 numeric
@attribute dim258 numeric
@attribute dim259 numeric
@attribute dim260 numeric
@attribute dim261 numeric
@attribute dim262 numeric
@attribute dim263 numeric
@attribute dim264 numeric
@attribute dim265 numeric
@attribute dim266 numeric
@attribute dim267 numeric
@attribute dim268 numeric
@attribute dim269 numeric
@attribute dim270 numeric
@attribute dim271 numeric
@attribute dim272 numeric
@attribute dim273 numeric
@attribute dim274 numeric
@attribute dim275 numeric
@attribute dim276 numeric
@attribute dim277 numeric
@attribute dim278 numeric
@attribute dim279 numeric
@attribute dim280 numeric
@attribute dim281 numeric
@attribute dim282 numeric
@attribute dim283 numeric
@attribute dim284 numeric
@attribute dim285 numeric
@attribute dim286 numeric
@attribute dim287 numeric
@attribute dim288 numeric
@attribute dim289 numeric
@attribute dim290 numeric
@attribute dim291 numeric
@attribute dim292 numeric
@attribute dim293 numeric
@attribute dim294 numeric
@attribute dim295 numeric
@attribute dim296 numeric
@attribute dim297 numeric
@attribute dim298 numeric
@attribute dim299 numeric
@attribute dim300 numeric
@attribute dim301 numeric
@attribute dim302 numeric
@attribute dim303 numeric
@attribute dim304 numeric
@attribute dim305 numeric
@attribute dim306 numeric
@attribute dim307 numeric
@attribute dim308 numeric
@attribute dim309 numeric
@attribute dim310 numeric
@attribute dim311 numeric
@attribute dim312 numeric
@attribute dim313 numeric
@attribute dim314 numeric
@attribute dim315 numeric
@attribute dim316 numeric
@attribute dim317 numeric
@attribute dim318 numeric
@attribute dim319 numeric
@attribute dim320 numeric
@attribute dim321 numeric
@attribute dim322 numeric
@attribute dim323 numeric
@attribute dim324 numeric
@attribute dim325 numeric
@attribute dim326 numeric
@attribute dim327 numeric
@attribute dim328 numeric
@attribute dim329 numeric
@attribute dim330 numeric
@attribute dim331 numeric
@attribute dim332 numeric
@attribute dim333 numeric
@attribute dim334 numeric
@attribute dim335 numeric
@attribute dim336 numeric
@attribute dim337 numeric
@attribute dim338 numeric
@attribute dim339 numeric
@attribute dim340 numeric
@attribute dim341 numeric
@attribute dim342 numeric
@attribute dim343 numeric
@attribute dim344 numeric
@attribute dim345 numeric
@attribute dim346 numeric
@attribute dim347 numeric
@attribute dim348 numeric
@attribute dim349 numeric
@attribute dim350 numeric
@attribute dim351 numeric
@attribute dim352 numeric
@attribute dim353 numeric
@attribute dim354 numeric
@attribute dim355 numeric
@attribute dim356 numeric
@attribute dim357 numeric
@attribute dim358 numeric
@attribute dim359 numeric
@attribute dim360 numeric
@attribute dim361 numeric
@attribute dim362 numeric
@attribute dim363 numeric
@attribute dim364 numeric
@attribute dim365 numeric
@attribute dim366 numeric
@attribute dim367 numeric
@attribute dim368 numeric
@attribute dim369 numeric
@attribute dim370 numeric
@attribute dim371 numeric
@attribute dim372 numeric
@attribute dim373 numeric
@attribute dim374 numeric
@attribute dim375 numeric
@attribute dim376 numeric
@attribute dim377 numeric
@attribute dim378 numeric
@attribute dim379 numeric
@attribute dim380 numeric
@attribute dim381 numeric
@attribute dim382 numeric
@attribute dim383 numeric
@attribute dim384 numeric
@attribute dim385 numeric
@attribute dim386 numeric
@attribute dim387 numeric
@attribute dim388 numeric
@attribute dim389 numeric
@attribute dim390 numeric
@attribute dim391 numeric
@attribute dim392 numeric
@attribute dim393 numeric
@attribute dim394 numeric
@attribute dim395 numeric
@attribute dim396 numeric
@attribute dim397 numeric
@attribute dim398 numeric
@attribute dim399 numeric
@attribute dim400 numeric
@attribute dim401 numeric
@attribute dim402 numeric
@attribute dim403 numeric
@attribute dim404 numeric
@attribute dim405 numeric
@attribute dim406 numeric
@attribute dim407 numeric
@attribute dim408 numeric
@attribute dim409 numeric
@attribute dim410 numeric
@attribute dim411 numeric
@attribute dim412 numeric
@attribute dim413 numeric
@attribute dim414 numeric
@attribute dim415 numeric
@attribute dim416 numeric
@attribute dim417 numeric
@attribute dim418 numeric
@attribute dim419 numeric
@attribute dim420 numeric
@attribute dim421 numeric
@attribute dim422 numeric
@attribute dim423 numeric
@attribute dim424 numeric
@attribute dim425 numeric
@attribute dim426 numeric
@attribute dim427 numeric
@attribute dim428 numeric
@attribute dim429 numeric
@attribute dim430 numeric
@attribute dim431 numeric
@attribute dim432 numeric
@attribute dim433 numeric
@attribute dim434 numeric
@attribute dim435 numeric
@attribute dim436 numeric
@attribute dim437 numeric
@attribute dim438 numeric
@attribute dim439 numeric
@attribute dim440 numeric
@attribute dim441 numeric
@attribute dim442 numeric
@attribute dim443 numeric
@attribute dim444 numeric
@attribute dim445 numeric
@attribute dim446 numeric
@attribute dim447 numeric
@attribute dim448 numeric
@attribute dim449 numeric
@attribute dim450 numeric
@attribute dim451 numeric
@attribute dim452 numeric
@attribute dim453 numeric
@attribute dim454 numeric
@attribute dim455 numeric
@attribute dim456 numeric
@attribute dim457 numeric
@attribute dim458 numeric
@attribute dim459 numeric
@attribute dim460 numeric
@attribute dim461 numeric
@attribute dim462 numeric
@attribute dim463 numeric
@attribute dim464 numeric
@attribute dim465 numeric
@attribute dim466 numeric
@attribute dim467 numeric
@attribute dim468 numeric
@attribute dim469 numeric
@attribute dim470 numeric
@attribute dim471 numeric
@attribute dim472 numeric
@attribute dim473 numeric
@attribute dim474 numeric
@attribute dim475 numeric
@attribute dim476 numeric
@attribute dim477 numeric
@attribute dim478 numeric
@attribute dim479 numeric
@attribute dim480 numeric
@attribute dim481 numeric
@attribute dim482 numeric
@attribute dim483 numeric
@attribute dim484 numeric
@attribute dim485 numeric
@attribute dim486 numeric
@attribute dim487 numeric
@attribute dim488 numeric
@attribute dim489 numeric
@attribute dim490 numeric
@attribute dim491 numeric
@attribute dim492 numeric
@attribute dim493 numeric
@attribute dim494 numeric
@attribute dim495 numeric
@attribute dim496 numeric
@attribute dim497 numeric
@attribute dim498 numeric
@attribute dim499 numeric
@attribute dim500 numeric
@attribute is_person {0,1}
@attribute '0.1  +  a23 * 0.2  + a88 * 0.13 + a92 * -0.11 + a97 * -0.1 + a123 * 0.08 + a130 * -0.09 + a171 * -0.05 + a206 * 0.06 + a311 * 0.1  + a387 * -0.05' numeric

@data


Classifier Model
Logistic Regression with ridge parameter of 1.0E-8
Coefficients...
                                                                                                                                                                     Class
Variable                                                                                                                                                                 0
==========================================================================================================================================================================
dim1                                                                                                                                                               -2.9605
dim2                                                                                                                                                               -0.5587
dim3                                                                                                                                                                6.1414
dim4                                                                                                                                                               -11.194
dim5                                                                                                                                                               -3.3055
dim6                                                                                                                                                                4.6282
dim7                                                                                                                                                               -2.9942
dim8                                                                                                                                                               -1.8498
dim9                                                                                                                                                                1.4442
dim10                                                                                                                                                               1.6822
dim11                                                                                                                                                               3.7681
dim12                                                                                                                                                               4.0997
dim13                                                                                                                                                              -4.5284
dim14                                                                                                                                                               1.7683
dim15                                                                                                                                                              -5.1783
dim16                                                                                                                                                               1.0568
dim17                                                                                                                                                              -6.3336
dim18                                                                                                                                                              -1.1535
dim19                                                                                                                                                               -3.808
dim20                                                                                                                                                               1.4332
dim21                                                                                                                                                             -13.1895
dim22                                                                                                                                                               6.3624
dim23                                                                                                                                                              -2.5703
dim24                                                                                                                                                              -0.0475
dim25                                                                                                                                                              -6.5025
dim26                                                                                                                                                               1.1339
dim27                                                                                                                                                              -3.8139
dim28                                                                                                                                                               3.9706
dim29                                                                                                                                                                -1.12
dim30                                                                                                                                                              -1.3902
dim31                                                                                                                                                               1.9829
dim32                                                                                                                                                               2.3157
dim33                                                                                                                                                              -1.9493
dim34                                                                                                                                                               1.9248
dim35                                                                                                                                                              -5.4485
dim36                                                                                                                                                              -1.6828
dim37                                                                                                                                                              -5.9379
dim38                                                                                                                                                             -10.2625
dim39                                                                                                                                                               2.5461
dim40                                                                                                                                                              -2.1116
dim41                                                                                                                                                              -3.1947
dim42                                                                                                                                                              -1.9104
dim43                                                                                                                                                               5.2955
dim44                                                                                                                                                              -5.3319
dim45                                                                                                                                                               6.0394
dim46                                                                                                                                                                0.767
dim47                                                                                                                                                              -0.9496
dim48                                                                                                                                                              -5.5596
dim49                                                                                                                                                               9.7851
dim50                                                                                                                                                              -3.4735
dim51                                                                                                                                                              -9.4558
dim52                                                                                                                                                              -2.0445
dim53                                                                                                                                                               4.6334
dim54                                                                                                                                                              -5.9974
dim55                                                                                                                                                               1.4896
dim56                                                                                                                                                               0.4796
dim57                                                                                                                                                              -0.3157
dim58                                                                                                                                                               -3.483
dim59                                                                                                                                                              -4.1854
dim60                                                                                                                                                                1.065
dim61                                                                                                                                                              -1.1712
dim62                                                                                                                                                              -4.9762
dim63                                                                                                                                                              -2.8218
dim64                                                                                                                                                              -2.2167
dim65                                                                                                                                                              -3.1261
dim66                                                                                                                                                              -3.8406
dim67                                                                                                                                                               3.6727
dim68                                                                                                                                                               2.4999
dim69                                                                                                                                                               0.6386
dim70                                                                                                                                                               5.9339
dim71                                                                                                                                                               9.4206
dim72                                                                                                                                                               0.4087
dim73                                                                                                                                                              -7.3151
dim74                                                                                                                                                              -7.6518
dim75                                                                                                                                                              -0.5324
dim76                                                                                                                                                              -9.3077
dim77                                                                                                                                                              -7.3585
dim78                                                                                                                                                               1.7097
dim79                                                                                                                                                              -2.8272
dim80                                                                                                                                                              -4.7245
dim81                                                                                                                                                              -7.2586
dim82                                                                                                                                                               3.9731
dim83                                                                                                                                                               1.6208
dim84                                                                                                                                                              -1.5493
dim85                                                                                                                                                              -3.1813
dim86                                                                                                                                                               4.7232
dim87                                                                                                                                                               3.1362
dim88                                                                                                                                                              14.1505
dim89                                                                                                                                                              -3.2919
dim90                                                                                                                                                              -9.1033
dim91                                                                                                                                                                0.643
dim92                                                                                                                                                             -14.4934
dim93                                                                                                                                                              -0.4666
dim94                                                                                                                                                              -3.3768
dim95                                                                                                                                                              -2.8725
dim96                                                                                                                                                              -9.1189
dim97                                                                                                                                                              -1.3295
dim98                                                                                                                                                               2.6577
dim99                                                                                                                                                               7.5913
dim100                                                                                                                                                              -0.693
dim101                                                                                                                                                              6.2096
dim102                                                                                                                                                             -0.5559
dim103                                                                                                                                                             -1.3439
dim104                                                                                                                                                             -0.2626
dim105                                                                                                                                                               3.944
dim106                                                                                                                                                              13.476
dim107                                                                                                                                                              4.9594
dim108                                                                                                                                                              4.0384
dim109                                                                                                                                                              5.3949
dim110                                                                                                                                                              3.4831
dim111                                                                                                                                                             -1.4096
dim112                                                                                                                                                             -0.6395
dim113                                                                                                                                                              0.8109
dim114                                                                                                                                                             -5.6054
dim115                                                                                                                                                             -0.7274
dim116                                                                                                                                                              5.0398
dim117                                                                                                                                                             -2.1652
dim118                                                                                                                                                             -0.5691
dim119                                                                                                                                                              3.2727
dim120                                                                                                                                                             -0.3764
dim121                                                                                                                                                             -2.1846
dim122                                                                                                                                                              2.0635
dim123                                                                                                                                                              3.1699
dim124                                                                                                                                                              4.8058
dim125                                                                                                                                                              3.8308
dim126                                                                                                                                                              5.3748
dim127                                                                                                                                                             -3.8063
dim128                                                                                                                                                             -0.1055
dim129                                                                                                                                                             -2.2371
dim130                                                                                                                                                              0.2322
dim131                                                                                                                                                             -2.8672
dim132                                                                                                                                                              3.2136
dim133                                                                                                                                                              -1.914
dim134                                                                                                                                                              -4.987
dim135                                                                                                                                                             16.5789
dim136                                                                                                                                                             -3.9685
dim137                                                                                                                                                             17.7751
dim138                                                                                                                                                             -3.1407
dim139                                                                                                                                                             -0.0885
dim140                                                                                                                                                              0.3572
dim141                                                                                                                                                              -1.221
dim142                                                                                                                                                            -11.3162
dim143                                                                                                                                                              4.9949
dim144                                                                                                                                                              7.3261
dim145                                                                                                                                                             -5.8507
dim146                                                                                                                                                             -2.6755
dim147                                                                                                                                                             -3.1294
dim148                                                                                                                                                             -2.4992
dim149                                                                                                                                                              0.4695
dim150                                                                                                                                                             -8.6202
dim151                                                                                                                                                              1.5637
dim152                                                                                                                                                             -2.8409
dim153                                                                                                                                                              -6.242
dim154                                                                                                                                                             -9.0206
dim155                                                                                                                                                             -3.1964
dim156                                                                                                                                                             -2.7515
dim157                                                                                                                                                             -5.0337
dim158                                                                                                                                                              2.7808
dim159                                                                                                                                                             -2.3869
dim160                                                                                                                                                              9.5904
dim161                                                                                                                                                             -2.2822
dim162                                                                                                                                                              4.2703
dim163                                                                                                                                                             -1.9965
dim164                                                                                                                                                              8.0025
dim165                                                                                                                                                             -0.2268
dim166                                                                                                                                                              5.6589
dim167                                                                                                                                                              5.7403
dim168                                                                                                                                                              0.5104
dim169                                                                                                                                                              2.7753
dim170                                                                                                                                                             -3.8441
dim171                                                                                                                                                             15.6044
dim172                                                                                                                                                              0.3729
dim173                                                                                                                                                             -2.2996
dim174                                                                                                                                                             -3.9629
dim175                                                                                                                                                              0.4596
dim176                                                                                                                                                             -0.0374
dim177                                                                                                                                                             -3.6446
dim178                                                                                                                                                              3.2564
dim179                                                                                                                                                              -4.698
dim180                                                                                                                                                             -2.3923
dim181                                                                                                                                                              6.2339
dim182                                                                                                                                                             -1.2225
dim183                                                                                                                                                              4.4391
dim184                                                                                                                                                             -1.6304
dim185                                                                                                                                                              -1.083
dim186                                                                                                                                                              2.0516
dim187                                                                                                                                                              2.4501
dim188                                                                                                                                                              6.9791
dim189                                                                                                                                                              0.7589
dim190                                                                                                                                                             -5.5953
dim191                                                                                                                                                              5.7566
dim192                                                                                                                                                              4.3523
dim193                                                                                                                                                             -1.6647
dim194                                                                                                                                                              11.203
dim195                                                                                                                                                             -2.9514
dim196                                                                                                                                                              4.6894
dim197                                                                                                                                                             -2.1063
dim198                                                                                                                                                             -1.0524
dim199                                                                                                                                                              2.7559
dim200                                                                                                                                                              1.7065
dim201                                                                                                                                                             16.4168
dim202                                                                                                                                                              9.4247
dim203                                                                                                                                                             -5.0549
dim204                                                                                                                                                              3.0355
dim205                                                                                                                                                             -0.1761
dim206                                                                                                                                                             15.0834
dim207                                                                                                                                                             -4.8544
dim208                                                                                                                                                             -3.4779
dim209                                                                                                                                                             -4.4078
dim210                                                                                                                                                              5.2621
dim211                                                                                                                                                             -0.9213
dim212                                                                                                                                                              8.7807
dim213                                                                                                                                                             -1.5536
dim214                                                                                                                                                             -5.9482
dim215                                                                                                                                                             -0.9101
dim216                                                                                                                                                             -5.2562
dim217                                                                                                                                                             -6.6915
dim218                                                                                                                                                              4.5388
dim219                                                                                                                                                             -4.3904
dim220                                                                                                                                                             -7.1358
dim221                                                                                                                                                             -6.3427
dim222                                                                                                                                                            -10.8415
dim223                                                                                                                                                             -1.3288
dim224                                                                                                                                                             -5.1889
dim225                                                                                                                                                             -0.1297
dim226                                                                                                                                                              4.5752
dim227                                                                                                                                                              2.8108
dim228                                                                                                                                                              0.9128
dim229                                                                                                                                                             -1.8874
dim230                                                                                                                                                              4.7584
dim231                                                                                                                                                             -5.2532
dim232                                                                                                                                                              1.1002
dim233                                                                                                                                                            -12.5674
dim234                                                                                                                                                              1.0683
dim235                                                                                                                                                              2.1785
dim236                                                                                                                                                              8.2624
dim237                                                                                                                                                               1.134
dim238                                                                                                                                                             -3.4689
dim239                                                                                                                                                             -1.4312
dim240                                                                                                                                                              6.9482
dim241                                                                                                                                                              0.9592
dim242                                                                                                                                                              -0.746
dim243                                                                                                                                                              1.9151
dim244                                                                                                                                                              4.3149
dim245                                                                                                                                                              3.7929
dim246                                                                                                                                                              4.7993
dim247                                                                                                                                                              4.3503
dim248                                                                                                                                                              1.0751
dim249                                                                                                                                                              4.3122
dim250                                                                                                                                                              1.6764
dim251                                                                                                                                                             -3.5489
dim252                                                                                                                                                             -2.4618
dim253                                                                                                                                                             -0.0305
dim254                                                                                                                                                             -6.6935
dim255                                                                                                                                                              0.2076
dim256                                                                                                                                                             -1.7136
dim257                                                                                                                                                             -4.8321
dim258                                                                                                                                                              4.3832
dim259                                                                                                                                                             -5.3795
dim260                                                                                                                                                              5.6618
dim261                                                                                                                                                             -2.9958
dim262                                                                                                                                                              2.6198
dim263                                                                                                                                                             -7.9675
dim264                                                                                                                                                              0.7856
dim265                                                                                                                                                             -1.9282
dim266                                                                                                                                                              2.4089
dim267                                                                                                                                                             -0.8113
dim268                                                                                                                                                              2.3154
dim269                                                                                                                                                              1.6394
dim270                                                                                                                                                             -0.1636
dim271                                                                                                                                                             -4.8913
dim272                                                                                                                                                             -5.9385
dim273                                                                                                                                                              1.6483
dim274                                                                                                                                                            -11.4877
dim275                                                                                                                                                              1.6855
dim276                                                                                                                                                             -0.3756
dim277                                                                                                                                                             -2.3459
dim278                                                                                                                                                              0.6436
dim279                                                                                                                                                             -3.3803
dim280                                                                                                                                                              3.8288
dim281                                                                                                                                                              1.9545
dim282                                                                                                                                                             -4.8638
dim283                                                                                                                                                             -6.9165
dim284                                                                                                                                                               3.521
dim285                                                                                                                                                              6.2823
dim286                                                                                                                                                             -2.4668
dim287                                                                                                                                                              1.2943
dim288                                                                                                                                                            -14.4668
dim289                                                                                                                                                             -6.3457
dim290                                                                                                                                                             -1.0536
dim291                                                                                                                                                             -2.8455
dim292                                                                                                                                                              4.7123
dim293                                                                                                                                                             -0.3215
dim294                                                                                                                                                             11.1193
dim295                                                                                                                                                             -3.6064
dim296                                                                                                                                                              1.9132
dim297                                                                                                                                                             -3.4579
dim298                                                                                                                                                              4.2114
dim299                                                                                                                                                             -5.2693
dim300                                                                                                                                                              4.8219
dim301                                                                                                                                                             -1.5983
dim302                                                                                                                                                             -4.2601
dim303                                                                                                                                                             -0.6272
dim304                                                                                                                                                              5.5505
dim305                                                                                                                                                              0.4051
dim306                                                                                                                                                              5.7042
dim307                                                                                                                                                               -2.61
dim308                                                                                                                                                             -6.7251
dim309                                                                                                                                                                2.32
dim310                                                                                                                                                             -2.5814
dim311                                                                                                                                                              9.0028
dim312                                                                                                                                                             -4.1059
dim313                                                                                                                                                             -2.3525
dim314                                                                                                                                                             -0.5217
dim315                                                                                                                                                             -6.1259
dim316                                                                                                                                                             -0.4974
dim317                                                                                                                                                              2.2058
dim318                                                                                                                                                              4.4075
dim319                                                                                                                                                             -3.3912
dim320                                                                                                                                                             -0.5652
dim321                                                                                                                                                             -6.4364
dim322                                                                                                                                                             -4.4915
dim323                                                                                                                                                             12.7697
dim324                                                                                                                                                             -2.1764
dim325                                                                                                                                                              3.8991
dim326                                                                                                                                                             -7.1203
dim327                                                                                                                                                             -0.1957
dim328                                                                                                                                                              0.9109
dim329                                                                                                                                                             -4.9366
dim330                                                                                                                                                             -5.2021
dim331                                                                                                                                                             -0.7472
dim332                                                                                                                                                              2.0262
dim333                                                                                                                                                              0.9818
dim334                                                                                                                                                              -0.373
dim335                                                                                                                                                              1.0753
dim336                                                                                                                                                              4.3452
dim337                                                                                                                                                             -3.1746
dim338                                                                                                                                                              0.0441
dim339                                                                                                                                                              5.5084
dim340                                                                                                                                                              1.9532
dim341                                                                                                                                                              4.3851
dim342                                                                                                                                                               2.767
dim343                                                                                                                                                               -11.7
dim344                                                                                                                                                             -4.7307
dim345                                                                                                                                                              2.2454
dim346                                                                                                                                                             -6.2522
dim347                                                                                                                                                             -4.8478
dim348                                                                                                                                                             -9.6032
dim349                                                                                                                                                             11.3547
dim350                                                                                                                                                               7.374
dim351                                                                                                                                                             -1.4603
dim352                                                                                                                                                             -6.3298
dim353                                                                                                                                                              -3.469
dim354                                                                                                                                                              9.9317
dim355                                                                                                                                                              6.0892
dim356                                                                                                                                                            -11.7796
dim357                                                                                                                                                             -4.4657
dim358                                                                                                                                                             -2.6463
dim359                                                                                                                                                              7.1118
dim360                                                                                                                                                            -10.7651
dim361                                                                                                                                                             -4.5416
dim362                                                                                                                                                             -1.7759
dim363                                                                                                                                                              6.6896
dim364                                                                                                                                                              5.4269
dim365                                                                                                                                                              -1.562
dim366                                                                                                                                                              1.1287
dim367                                                                                                                                                             -1.9798
dim368                                                                                                                                                              -3.852
dim369                                                                                                                                                               5.213
dim370                                                                                                                                                             -0.7755
dim371                                                                                                                                                             -2.1611
dim372                                                                                                                                                              3.0933
dim373                                                                                                                                                              6.5705
dim374                                                                                                                                                              1.7693
dim375                                                                                                                                                              4.6485
dim376                                                                                                                                                              3.1758
dim377                                                                                                                                                             -4.8652
dim378                                                                                                                                                              2.4074
dim379                                                                                                                                                              1.7884
dim380                                                                                                                                                              6.0665
dim381                                                                                                                                                             -3.2853
dim382                                                                                                                                                             -2.3541
dim383                                                                                                                                                             -2.3164
dim384                                                                                                                                                              5.5095
dim385                                                                                                                                                              5.1146
dim386                                                                                                                                                              3.1151
dim387                                                                                                                                                              6.1537
dim388                                                                                                                                                             -0.7816
dim389                                                                                                                                                              1.9273
dim390                                                                                                                                                             -1.9751
dim391                                                                                                                                                              8.0177
dim392                                                                                                                                                             -1.1784
dim393                                                                                                                                                             -7.2619
dim394                                                                                                                                                              1.1208
dim395                                                                                                                                                             -0.8171
dim396                                                                                                                                                             -1.6912
dim397                                                                                                                                                              3.7557
dim398                                                                                                                                                            -10.4564
dim399                                                                                                                                                             -1.0976
dim400                                                                                                                                                             -1.2261
dim401                                                                                                                                                              1.0445
dim402                                                                                                                                                             -1.7607
dim403                                                                                                                                                             -5.0234
dim404                                                                                                                                                             -4.4515
dim405                                                                                                                                                              0.5915
dim406                                                                                                                                                             -5.2876
dim407                                                                                                                                                               2.628
dim408                                                                                                                                                             -0.4856
dim409                                                                                                                                                             -0.1617
dim410                                                                                                                                                             -0.4372
dim411                                                                                                                                                              0.4356
dim412                                                                                                                                                                -2.2
dim413                                                                                                                                                             -2.9695
dim414                                                                                                                                                             -1.8371
dim415                                                                                                                                                             -2.9066
dim416                                                                                                                                                             -3.3456
dim417                                                                                                                                                             -0.3101
dim418                                                                                                                                                              3.0123
dim419                                                                                                                                                              0.1696
dim420                                                                                                                                                             -2.4369
dim421                                                                                                                                                              2.3425
dim422                                                                                                                                                              3.2618
dim423                                                                                                                                                             -4.7344
dim424                                                                                                                                                              6.1077
dim425                                                                                                                                                              0.0438
dim426                                                                                                                                                             -0.9851
dim427                                                                                                                                                             -6.4626
dim428                                                                                                                                                             10.0275
dim429                                                                                                                                                             -2.3148
dim430                                                                                                                                                             -2.5988
dim431                                                                                                                                                              0.5064
dim432                                                                                                                                                              1.6473
dim433                                                                                                                                                              3.9317
dim434                                                                                                                                                              4.9488
dim435                                                                                                                                                              3.9292
dim436                                                                                                                                                             -2.4256
dim437                                                                                                                                                              3.4297
dim438                                                                                                                                                               2.428
dim439                                                                                                                                                             -1.4023
dim440                                                                                                                                                             -3.5062
dim441                                                                                                                                                              6.8268
dim442                                                                                                                                                             -3.6933
dim443                                                                                                                                                              5.8821
dim444                                                                                                                                                             -6.0783
dim445                                                                                                                                                             -0.0269
dim446                                                                                                                                                              0.5984
dim447                                                                                                                                                             -3.1592
dim448                                                                                                                                                              3.2885
dim449                                                                                                                                                              2.8008
dim450                                                                                                                                                               2.263
dim451                                                                                                                                                               4.589
dim452                                                                                                                                                              3.5599
dim453                                                                                                                                                              2.1611
dim454                                                                                                                                                              3.3244
dim455                                                                                                                                                             -0.8013
dim456                                                                                                                                                              0.6182
dim457                                                                                                                                                             -0.8434
dim458                                                                                                                                                              0.2181
dim459                                                                                                                                                             -7.8413
dim460                                                                                                                                                             -4.5381
dim461                                                                                                                                                              -9.825
dim462                                                                                                                                                             -3.8005
dim463                                                                                                                                                              2.7427
dim464                                                                                                                                                              1.2691
dim465                                                                                                                                                             10.8326
dim466                                                                                                                                                              5.5454
dim467                                                                                                                                                              7.4737
dim468                                                                                                                                                              2.5451
dim469                                                                                                                                                              3.2689
dim470                                                                                                                                                             -1.1087
dim471                                                                                                                                                             -0.1529
dim472                                                                                                                                                              0.1553
dim473                                                                                                                                                             -0.8707
dim474                                                                                                                                                              0.9913
dim475                                                                                                                                                              1.2448
dim476                                                                                                                                                              -0.148
dim477                                                                                                                                                              0.2955
dim478                                                                                                                                                             -2.0839
dim479                                                                                                                                                            -10.7477
dim480                                                                                                                                                              9.9808
dim481                                                                                                                                                              2.6081
dim482                                                                                                                                                              0.8267
dim483                                                                                                                                                             -4.7685
dim484                                                                                                                                                              0.3729
dim485                                                                                                                                                             -8.3279
dim486                                                                                                                                                             -1.8595
dim487                                                                                                                                                             -1.5344
dim488                                                                                                                                                             -3.1712
dim489                                                                                                                                                              2.7769
dim490                                                                                                                                                              7.5903
dim491                                                                                                                                                             -0.5722
dim492                                                                                                                                                              0.3578
dim493                                                                                                                                                              9.9472
dim494                                                                                                                                                              0.7859
dim495                                                                                                                                                              5.6392
dim496                                                                                                                                                              3.0293
dim497                                                                                                                                                              4.7483
dim498                                                                                                                                                             -1.7456
dim499                                                                                                                                                              3.6559
dim500                                                                                                                                                             14.1448
0.1  +  a23 * 0.2  + a88 * 0.13 + a92 * -0.11 + a97 * -0.1 + a123 * 0.08 + a130 * -0.09 + a171 * -0.05 + a206 * 0.06 + a311 * 0.1  + a387 * -0.05                  40.9462
Intercept                                                                                                                                                          -4.6633


Odds Ratios...
                                                                                                                                                                     Class
Variable                                                                                                                                                                 0
==========================================================================================================================================================================
dim1                                                                                                                                                                0.0518
dim2                                                                                                                                                                0.5719
dim3                                                                                                                                                              464.7143
dim4                                                                                                                                                                     0
dim5                                                                                                                                                                0.0367
dim6                                                                                                                                                              102.3286
dim7                                                                                                                                                                0.0501
dim8                                                                                                                                                                0.1573
dim9                                                                                                                                                                4.2385
dim10                                                                                                                                                               5.3772
dim11                                                                                                                                                              43.2991
dim12                                                                                                                                                              60.3238
dim13                                                                                                                                                               0.0108
dim14                                                                                                                                                               5.8608
dim15                                                                                                                                                               0.0056
dim16                                                                                                                                                               2.8772
dim17                                                                                                                                                               0.0018
dim18                                                                                                                                                               0.3155
dim19                                                                                                                                                               0.0222
dim20                                                                                                                                                               4.1922
dim21                                                                                                                                                                    0
dim22                                                                                                                                                             579.6517
dim23                                                                                                                                                               0.0765
dim24                                                                                                                                                               0.9536
dim25                                                                                                                                                               0.0015
dim26                                                                                                                                                               3.1076
dim27                                                                                                                                                               0.0221
dim28                                                                                                                                                              53.0148
dim29                                                                                                                                                               0.3263
dim30                                                                                                                                                                0.249
dim31                                                                                                                                                                7.264
dim32                                                                                                                                                              10.1315
dim33                                                                                                                                                               0.1424
dim34                                                                                                                                                               6.8535
dim35                                                                                                                                                               0.0043
dim36                                                                                                                                                               0.1858
dim37                                                                                                                                                               0.0026
dim38                                                                                                                                                                    0
dim39                                                                                                                                                               12.757
dim40                                                                                                                                                                0.121
dim41                                                                                                                                                                0.041
dim42                                                                                                                                                                0.148
dim43                                                                                                                                                             199.4444
dim44                                                                                                                                                               0.0048
dim45                                                                                                                                                             419.6368
dim46                                                                                                                                                               2.1532
dim47                                                                                                                                                               0.3869
dim48                                                                                                                                                               0.0039
dim49                                                                                                                                                           17766.3121
dim50                                                                                                                                                                0.031
dim51                                                                                                                                                               0.0001
dim52                                                                                                                                                               0.1294
dim53                                                                                                                                                             102.8672
dim54                                                                                                                                                               0.0025
dim55                                                                                                                                                               4.4353
dim56                                                                                                                                                               1.6154
dim57                                                                                                                                                               0.7293
dim58                                                                                                                                                               0.0307
dim59                                                                                                                                                               0.0152
dim60                                                                                                                                                               2.9009
dim61                                                                                                                                                                 0.31
dim62                                                                                                                                                               0.0069
dim63                                                                                                                                                               0.0595
dim64                                                                                                                                                                0.109
dim65                                                                                                                                                               0.0439
dim66                                                                                                                                                               0.0215
dim67                                                                                                                                                              39.3582
dim68                                                                                                                                                              12.1813
dim69                                                                                                                                                               1.8938
dim70                                                                                                                                                             377.6123
dim71                                                                                                                                                           12339.9424
dim72                                                                                                                                                               1.5049
dim73                                                                                                                                                               0.0007
dim74                                                                                                                                                               0.0005
dim75                                                                                                                                                               0.5872
dim76                                                                                                                                                               0.0001
dim77                                                                                                                                                               0.0006
dim78                                                                                                                                                                5.527
dim79                                                                                                                                                               0.0592
dim80                                                                                                                                                               0.0089
dim81                                                                                                                                                               0.0007
dim82                                                                                                                                                              53.1478
dim83                                                                                                                                                               5.0571
dim84                                                                                                                                                               0.2124
dim85                                                                                                                                                               0.0415
dim86                                                                                                                                                             112.5231
dim87                                                                                                                                                              23.0163
dim88                                                                                                                                                         1397983.8065
dim89                                                                                                                                                               0.0372
dim90                                                                                                                                                               0.0001
dim91                                                                                                                                                               1.9022
dim92                                                                                                                                                                    0
dim93                                                                                                                                                               0.6271
dim94                                                                                                                                                               0.0342
dim95                                                                                                                                                               0.0566
dim96                                                                                                                                                               0.0001
dim97                                                                                                                                                               0.2646
dim98                                                                                                                                                               14.263
dim99                                                                                                                                                            1980.8554
dim100                                                                                                                                                                 0.5
dim101                                                                                                                                                            497.4929
dim102                                                                                                                                                              0.5735
dim103                                                                                                                                                              0.2608
dim104                                                                                                                                                               0.769
dim105                                                                                                                                                             51.6267
dim106                                                                                                                                                         712128.1262
dim107                                                                                                                                                            142.5088
dim108                                                                                                                                                             56.7375
dim109                                                                                                                                                            220.2804
dim110                                                                                                                                                             32.5617
dim111                                                                                                                                                              0.2442
dim112                                                                                                                                                              0.5276
dim113                                                                                                                                                                2.25
dim114                                                                                                                                                              0.0037
dim115                                                                                                                                                              0.4831
dim116                                                                                                                                                            154.4462
dim117                                                                                                                                                              0.1147
dim118                                                                                                                                                               0.566
dim119                                                                                                                                                             26.3837
dim120                                                                                                                                                              0.6863
dim121                                                                                                                                                              0.1125
dim122                                                                                                                                                              7.8735
dim123                                                                                                                                                             23.8056
dim124                                                                                                                                                            122.2183
dim125                                                                                                                                                             46.0972
dim126                                                                                                                                                            215.9005
dim127                                                                                                                                                              0.0222
dim128                                                                                                                                                              0.8999
dim129                                                                                                                                                              0.1068
dim130                                                                                                                                                              1.2613
dim131                                                                                                                                                              0.0569
dim132                                                                                                                                                             24.8681
dim133                                                                                                                                                              0.1475
dim134                                                                                                                                                              0.0068
dim135                                                                                                                                                       15853141.3737
dim136                                                                                                                                                              0.0189
dim137                                                                                                                                                       52437443.8416
dim138                                                                                                                                                              0.0433
dim139                                                                                                                                                              0.9153
dim140                                                                                                                                                              1.4294
dim141                                                                                                                                                              0.2949
dim142                                                                                                                                                                   0
dim143                                                                                                                                                            147.6647
dim144                                                                                                                                                           1519.4743
dim145                                                                                                                                                              0.0029
dim146                                                                                                                                                              0.0689
dim147                                                                                                                                                              0.0437
dim148                                                                                                                                                              0.0822
dim149                                                                                                                                                              1.5991
dim150                                                                                                                                                              0.0002
dim151                                                                                                                                                              4.7763
dim152                                                                                                                                                              0.0584
dim153                                                                                                                                                              0.0019
dim154                                                                                                                                                              0.0001
dim155                                                                                                                                                              0.0409
dim156                                                                                                                                                              0.0638
dim157                                                                                                                                                              0.0065
dim158                                                                                                                                                             16.1311
dim159                                                                                                                                                              0.0919
dim160                                                                                                                                                          14624.0093
dim161                                                                                                                                                              0.1021
dim162                                                                                                                                                              71.542
dim163                                                                                                                                                              0.1358
dim164                                                                                                                                                           2988.4015
dim165                                                                                                                                                              0.7971
dim166                                                                                                                                                            286.8256
dim167                                                                                                                                                            311.1685
dim168                                                                                                                                                              1.6659
dim169                                                                                                                                                             16.0437
dim170                                                                                                                                                              0.0214
dim171                                                                                                                                                         5983067.302
dim172                                                                                                                                                              1.4519
dim173                                                                                                                                                              0.1003
dim174                                                                                                                                                               0.019
dim175                                                                                                                                                              1.5834
dim176                                                                                                                                                              0.9633
dim177                                                                                                                                                              0.0261
dim178                                                                                                                                                              25.957
dim179                                                                                                                                                              0.0091
dim180                                                                                                                                                              0.0914
dim181                                                                                                                                                            509.7303
dim182                                                                                                                                                              0.2945
dim183                                                                                                                                                             84.6952
dim184                                                                                                                                                              0.1959
dim185                                                                                                                                                              0.3386
dim186                                                                                                                                                              7.7805
dim187                                                                                                                                                             11.5894
dim188                                                                                                                                                           1073.9022
dim189                                                                                                                                                              2.1359
dim190                                                                                                                                                              0.0037
dim191                                                                                                                                                            316.2664
dim192                                                                                                                                                             77.6592
dim193                                                                                                                                                              0.1893
dim194                                                                                                                                                           73347.523
dim195                                                                                                                                                              0.0523
dim196                                                                                                                                                             108.786
dim197                                                                                                                                                              0.1217
dim198                                                                                                                                                              0.3491
dim199                                                                                                                                                             15.7348
dim200                                                                                                                                                              5.5098
dim201                                                                                                                                                       13480492.3335
dim202                                                                                                                                                          12390.8882
dim203                                                                                                                                                              0.0064
dim204                                                                                                                                                             20.8113
dim205                                                                                                                                                              0.8385
dim206                                                                                                                                                        3553324.9632
dim207                                                                                                                                                              0.0078
dim208                                                                                                                                                              0.0309
dim209                                                                                                                                                              0.0122
dim210                                                                                                                                                            192.8796
dim211                                                                                                                                                               0.398
dim212                                                                                                                                                           6507.1127
dim213                                                                                                                                                              0.2115
dim214                                                                                                                                                              0.0026
dim215                                                                                                                                                              0.4025
dim216                                                                                                                                                              0.0052
dim217                                                                                                                                                              0.0012
dim218                                                                                                                                                             93.5762
dim219                                                                                                                                                              0.0124
dim220                                                                                                                                                              0.0008
dim221                                                                                                                                                              0.0018
dim222                                                                                                                                                                   0
dim223                                                                                                                                                              0.2648
dim224                                                                                                                                                              0.0056
dim225                                                                                                                                                              0.8784
dim226                                                                                                                                                             97.0468
dim227                                                                                                                                                             16.6224
dim228                                                                                                                                                              2.4912
dim229                                                                                                                                                              0.1515
dim230                                                                                                                                                            116.5566
dim231                                                                                                                                                              0.0052
dim232                                                                                                                                                              3.0048
dim233                                                                                                                                                                   0
dim234                                                                                                                                                              2.9103
dim235                                                                                                                                                              8.8331
dim236                                                                                                                                                           3875.3656
dim237                                                                                                                                                              3.1082
dim238                                                                                                                                                              0.0312
dim239                                                                                                                                                               0.239
dim240                                                                                                                                                           1041.2327
dim241                                                                                                                                                              2.6096
dim242                                                                                                                                                              0.4742
dim243                                                                                                                                                              6.7873
dim244                                                                                                                                                             74.8032
dim245                                                                                                                                                             44.3837
dim246                                                                                                                                                            121.4202
dim247                                                                                                                                                             77.5005
dim248                                                                                                                                                              2.9303
dim249                                                                                                                                                             74.6024
dim250                                                                                                                                                               5.346
dim251                                                                                                                                                              0.0288
dim252                                                                                                                                                              0.0853
dim253                                                                                                                                                              0.9699
dim254                                                                                                                                                              0.0012
dim255                                                                                                                                                              1.2307
dim256                                                                                                                                                              0.1802
dim257                                                                                                                                                               0.008
dim258                                                                                                                                                             80.0966
dim259                                                                                                                                                              0.0046
dim260                                                                                                                                                            287.6627
dim261                                                                                                                                                                0.05
dim262                                                                                                                                                             13.7335
dim263                                                                                                                                                              0.0003
dim264                                                                                                                                                              2.1937
dim265                                                                                                                                                              0.1454
dim266                                                                                                                                                             11.1216
dim267                                                                                                                                                              0.4443
dim268                                                                                                                                                             10.1286
dim269                                                                                                                                                              5.1522
dim270                                                                                                                                                               0.849
dim271                                                                                                                                                              0.0075
dim272                                                                                                                                                              0.0026
dim273                                                                                                                                                              5.1979
dim274                                                                                                                                                                   0
dim275                                                                                                                                                              5.3952
dim276                                                                                                                                                              0.6869
dim277                                                                                                                                                              0.0958
dim278                                                                                                                                                              1.9034
dim279                                                                                                                                                               0.034
dim280                                                                                                                                                             46.0071
dim281                                                                                                                                                                7.06
dim282                                                                                                                                                              0.0077
dim283                                                                                                                                                               0.001
dim284                                                                                                                                                             33.8194
dim285                                                                                                                                                            535.0332
dim286                                                                                                                                                              0.0849
dim287                                                                                                                                                              3.6484
dim288                                                                                                                                                                   0
dim289                                                                                                                                                              0.0018
dim290                                                                                                                                                              0.3487
dim291                                                                                                                                                              0.0581
dim292                                                                                                                                                            111.3097
dim293                                                                                                                                                               0.725
dim294                                                                                                                                                          67463.7361
dim295                                                                                                                                                              0.0272
dim296                                                                                                                                                              6.7749
dim297                                                                                                                                                              0.0315
dim298                                                                                                                                                             67.4481
dim299                                                                                                                                                              0.0051
dim300                                                                                                                                                            124.1965
dim301                                                                                                                                                              0.2022
dim302                                                                                                                                                              0.0141
dim303                                                                                                                                                              0.5341
dim304                                                                                                                                                            257.3754
dim305                                                                                                                                                              1.4995
dim306                                                                                                                                                             300.127
dim307                                                                                                                                                              0.0735
dim308                                                                                                                                                              0.0012
dim309                                                                                                                                                             10.1755
dim310                                                                                                                                                              0.0757
dim311                                                                                                                                                           8125.6518
dim312                                                                                                                                                              0.0165
dim313                                                                                                                                                              0.0951
dim314                                                                                                                                                              0.5935
dim315                                                                                                                                                              0.0022
dim316                                                                                                                                                              0.6081
dim317                                                                                                                                                              9.0779
dim318                                                                                                                                                             82.0663
dim319                                                                                                                                                              0.0337
dim320                                                                                                                                                              0.5682
dim321                                                                                                                                                              0.0016
dim322                                                                                                                                                              0.0112
dim323                                                                                                                                                         351420.2183
dim324                                                                                                                                                              0.1135
dim325                                                                                                                                                             49.3602
dim326                                                                                                                                                              0.0008
dim327                                                                                                                                                              0.8223
dim328                                                                                                                                                              2.4866
dim329                                                                                                                                                              0.0072
dim330                                                                                                                                                              0.0055
dim331                                                                                                                                                              0.4737
dim332                                                                                                                                                               7.585
dim333                                                                                                                                                              2.6693
dim334                                                                                                                                                              0.6887
dim335                                                                                                                                                              2.9308
dim336                                                                                                                                                             77.1074
dim337                                                                                                                                                              0.0418
dim338                                                                                                                                                              1.0451
dim339                                                                                                                                                              246.76
dim340                                                                                                                                                              7.0513
dim341                                                                                                                                                             80.2496
dim342                                                                                                                                                             15.9115
dim343                                                                                                                                                                   0
dim344                                                                                                                                                              0.0088
dim345                                                                                                                                                              9.4445
dim346                                                                                                                                                              0.0019
dim347                                                                                                                                                              0.0078
dim348                                                                                                                                                              0.0001
dim349                                                                                                                                                          85361.7505
dim350                                                                                                                                                           1593.9581
dim351                                                                                                                                                              0.2322
dim352                                                                                                                                                              0.0018
dim353                                                                                                                                                              0.0311
dim354                                                                                                                                                          20572.3602
dim355                                                                                                                                                            441.0558
dim356                                                                                                                                                                   0
dim357                                                                                                                                                              0.0115
dim358                                                                                                                                                              0.0709
dim359                                                                                                                                                           1226.3342
dim360                                                                                                                                                                   0
dim361                                                                                                                                                              0.0107
dim362                                                                                                                                                              0.1693
dim363                                                                                                                                                            803.9645
dim364                                                                                                                                                            227.4405
dim365                                                                                                                                                              0.2097
dim366                                                                                                                                                              3.0917
dim367                                                                                                                                                              0.1381
dim368                                                                                                                                                              0.0212
dim369                                                                                                                                                            183.6367
dim370                                                                                                                                                              0.4605
dim371                                                                                                                                                              0.1152
dim372                                                                                                                                                             22.0489
dim373                                                                                                                                                            713.7388
dim374                                                                                                                                                               5.867
dim375                                                                                                                                                             104.433
dim376                                                                                                                                                             23.9457
dim377                                                                                                                                                              0.0077
dim378                                                                                                                                                             11.1048
dim379                                                                                                                                                              5.9797
dim380                                                                                                                                                            431.1674
dim381                                                                                                                                                              0.0374
dim382                                                                                                                                                               0.095
dim383                                                                                                                                                              0.0986
dim384                                                                                                                                                            247.0264
dim385                                                                                                                                                            166.4363
dim386                                                                                                                                                             22.5356
dim387                                                                                                                                                            470.4375
dim388                                                                                                                                                              0.4577
dim389                                                                                                                                                              6.8708
dim390                                                                                                                                                              0.1388
dim391                                                                                                                                                           3034.3247
dim392                                                                                                                                                              0.3078
dim393                                                                                                                                                              0.0007
dim394                                                                                                                                                              3.0672
dim395                                                                                                                                                              0.4417
dim396                                                                                                                                                              0.1843
dim397                                                                                                                                                             42.7627
dim398                                                                                                                                                                   0
dim399                                                                                                                                                              0.3337
dim400                                                                                                                                                              0.2934
dim401                                                                                                                                                              2.8419
dim402                                                                                                                                                              0.1719
dim403                                                                                                                                                              0.0066
dim404                                                                                                                                                              0.0117
dim405                                                                                                                                                              1.8067
dim406                                                                                                                                                              0.0051
dim407                                                                                                                                                             13.8456
dim408                                                                                                                                                              0.6153
dim409                                                                                                                                                              0.8507
dim410                                                                                                                                                              0.6458
dim411                                                                                                                                                              1.5458
dim412                                                                                                                                                              0.1108
dim413                                                                                                                                                              0.0513
dim414                                                                                                                                                              0.1593
dim415                                                                                                                                                              0.0547
dim416                                                                                                                                                              0.0352
dim417                                                                                                                                                              0.7334
dim418                                                                                                                                                             20.3342
dim419                                                                                                                                                              1.1848
dim420                                                                                                                                                              0.0874
dim421                                                                                                                                                             10.4071
dim422                                                                                                                                                             26.0956
dim423                                                                                                                                                              0.0088
dim424                                                                                                                                                            449.3062
dim425                                                                                                                                                              1.0447
dim426                                                                                                                                                              0.3734
dim427                                                                                                                                                              0.0016
dim428                                                                                                                                                          22641.1361
dim429                                                                                                                                                              0.0988
dim430                                                                                                                                                              0.0744
dim431                                                                                                                                                              1.6594
dim432                                                                                                                                                              5.1928
dim433                                                                                                                                                              50.993
dim434                                                                                                                                                            141.0122
dim435                                                                                                                                                             50.8642
dim436                                                                                                                                                              0.0884
dim437                                                                                                                                                             30.8673
dim438                                                                                                                                                             11.3362
dim439                                                                                                                                                               0.246
dim440                                                                                                                                                                0.03
dim441                                                                                                                                                            922.2778
dim442                                                                                                                                                              0.0249
dim443                                                                                                                                                            358.5576
dim444                                                                                                                                                              0.0023
dim445                                                                                                                                                              0.9735
dim446                                                                                                                                                              1.8191
dim447                                                                                                                                                              0.0425
dim448                                                                                                                                                             26.8019
dim449                                                                                                                                                             16.4572
dim450                                                                                                                                                              9.6118
dim451                                                                                                                                                             98.3974
dim452                                                                                                                                                             35.1599
dim453                                                                                                                                                              8.6805
dim454                                                                                                                                                             27.7832
dim455                                                                                                                                                              0.4488
dim456                                                                                                                                                              1.8556
dim457                                                                                                                                                              0.4303
dim458                                                                                                                                                              1.2437
dim459                                                                                                                                                              0.0004
dim460                                                                                                                                                              0.0107
dim461                                                                                                                                                              0.0001
dim462                                                                                                                                                              0.0224
dim463                                                                                                                                                             15.5287
dim464                                                                                                                                                              3.5576
dim465                                                                                                                                                          50644.4431
dim466                                                                                                                                                            256.0443
dim467                                                                                                                                                           1761.1339
dim468                                                                                                                                                             12.7447
dim469                                                                                                                                                             26.2813
dim470                                                                                                                                                                0.33
dim471                                                                                                                                                              0.8582
dim472                                                                                                                                                               1.168
dim473                                                                                                                                                              0.4187
dim474                                                                                                                                                              2.6947
dim475                                                                                                                                                              3.4721
dim476                                                                                                                                                              0.8624
dim477                                                                                                                                                              1.3438
dim478                                                                                                                                                              0.1244
dim479                                                                                                                                                                   0
dim480                                                                                                                                                          21607.1893
dim481                                                                                                                                                             13.5736
dim482                                                                                                                                                              2.2857
dim483                                                                                                                                                              0.0085
dim484                                                                                                                                                               1.452
dim485                                                                                                                                                              0.0002
dim486                                                                                                                                                              0.1558
dim487                                                                                                                                                              0.2156
dim488                                                                                                                                                               0.042
dim489                                                                                                                                                             16.0695
dim490                                                                                                                                                           1978.9684
dim491                                                                                                                                                              0.5643
dim492                                                                                                                                                              1.4302
dim493                                                                                                                                                          20892.7641
dim494                                                                                                                                                              2.1945
dim495                                                                                                                                                            281.2359
dim496                                                                                                                                                             20.6837
dim497                                                                                                                                                            115.3872
dim498                                                                                                                                                              0.1745
dim499                                                                                                                                                             38.7042
dim500                                                                                                                                                        1390033.2432
0.1  +  a23 * 0.2  + a88 * 0.13 + a92 * -0.11 + a97 * -0.1 + a123 * 0.08 + a130 * -0.09 + a171 * -0.05 + a206 * 0.06 + a311 * 0.1  + a387 * -0.05    6.0632440594533722E17


Time taken to build model: 0.76 seconds

=== Predictions ontest split===

inst#,    actual, predicted, error, probability distribution
     1        2:1        2:1          0.355 *0.645
     2        1:0        2:1      +   0.427 *0.573
     3        1:0        2:1      +   0.362 *0.638
     4        1:0        1:0         *0.752  0.248
     5        1:0        2:1      +   0.447 *0.553
     6        2:1        2:1          0.45  *0.55 
     7        2:1        1:0      +  *0.729  0.271
     8        2:1        2:1          0.475 *0.525
     9        2:1        2:1          0.305 *0.695
    10        1:0        1:0         *0.693  0.307
    11        2:1        2:1          0.322 *0.678
    12        2:1        2:1          0.245 *0.755
    13        1:0        1:0         *0.526  0.474
    14        2:1        1:0      +  *0.745  0.255
    15        1:0        1:0         *0.605  0.395
    16        1:0        1:0         *0.829  0.171
    17        2:1        1:0      +  *0.577  0.423
    18        1:0        1:0         *0.858  0.142
    19        1:0        2:1      +   0.163 *0.837
    20        1:0        2:1      +   0.414 *0.586
    21        1:0        1:0         *0.901  0.099
    22        1:0        1:0         *0.515  0.485
    23        2:1        1:0      +  *0.593  0.407
    24        1:0        1:0         *0.871  0.129
    25        2:1        2:1          0.149 *0.851
    26        1:0        1:0         *0.748  0.252
    27        2:1        1:0      +  *0.615  0.385
    28        2:1        1:0      +  *0.653  0.347
    29        2:1        2:1          0.453 *0.547
    30        1:0        1:0         *0.728  0.272
    31        2:1        1:0      +  *0.615  0.385
    32        2:1        1:0      +  *0.652  0.348
    33        1:0        2:1      +   0.405 *0.595
    34        2:1        1:0      +  *0.531  0.469
    35        2:1        2:1          0.211 *0.789
    36        1:0        1:0         *0.624  0.376
    37        1:0        2:1      +   0.454 *0.546
    38        1:0        2:1      +   0.113 *0.887
    39        1:0        1:0         *0.57   0.43 
    40        2:1        2:1          0.057 *0.943
    41        2:1        1:0      +  *0.58   0.42 
    42        2:1        1:0      +  *0.68   0.32 
    43        1:0        2:1      +   0.355 *0.645
    44        2:1        1:0      +  *0.708  0.292
    45        1:0        1:0         *0.615  0.385
    46        2:1        2:1          0.433 *0.567
    47        2:1        2:1          0.453 *0.547
    48        1:0        1:0         *0.717  0.283
    49        2:1        2:1          0.315 *0.685
    50        1:0        1:0         *0.697  0.303
    51        1:0        1:0         *0.634  0.366
    52        2:1        1:0      +  *0.615  0.385
    53        2:1        2:1          0.366 *0.634
    54        2:1        1:0      +  *0.574  0.426
    55        1:0        1:0         *0.667  0.333
    56        1:0        2:1      +   0.315 *0.685
    57        2:1        1:0      +  *0.501  0.499
    58        2:1        1:0      +  *0.54   0.46 
    59        2:1        1:0      +  *0.573  0.427
    60        2:1        2:1          0.477 *0.523
    61        1:0        1:0         *0.765  0.235
    62        1:0        1:0         *0.65   0.35 
    63        2:1        2:1          0.378 *0.622
    64        2:1        1:0      +  *0.73   0.27 
    65        2:1        1:0      +  *0.867  0.133
    66        2:1        1:0      +  *0.533  0.467
    67        1:0        1:0         *0.958  0.042
    68        2:1        1:0      +  *0.883  0.117
    69        1:0        2:1      +   0.247 *0.753
    70        2:1        1:0      +  *0.88   0.12 
    71        1:0        1:0         *0.908  0.092
    72        1:0        1:0         *0.621  0.379
    73        2:1        2:1          0.299 *0.701
    74        1:0        1:0         *0.704  0.296
    75        1:0        1:0         *0.909  0.091
    76        1:0        1:0         *0.722  0.278
    77        1:0        1:0         *0.853  0.147
    78        2:1        2:1          0.473 *0.527
    79        2:1        2:1          0.221 *0.779
    80        2:1        1:0      +  *0.605  0.395
    81        1:0        2:1      +   0.45  *0.55 
    82        2:1        2:1          0.485 *0.515
    83        1:0        2:1      +   0.241 *0.759
    84        1:0        1:0         *0.68   0.32 
    85        2:1        2:1          0.439 *0.561
    86        2:1        2:1          0.459 *0.541
    87        2:1        2:1          0.45  *0.55 
    88        2:1        1:0      +  *0.694  0.306
    89        2:1        2:1          0.347 *0.653
    90        1:0        1:0         *1      0    
    91        1:0        1:0         *0.684  0.316
    92        2:1        2:1          0.418 *0.582
    93        2:1        2:1          0.105 *0.895
    94        1:0        2:1      +   0.286 *0.714
    95        2:1        2:1          0.35  *0.65 
    96        1:0        2:1      +   0.372 *0.628
    97        1:0        1:0         *0.872  0.128
    98        1:0        1:0         *0.655  0.345
    99        1:0        1:0         *0.73   0.27 
   100        2:1        1:0      +  *0.627  0.373
   101        1:0        2:1      +   0.467 *0.533
   102        1:0        1:0         *0.716  0.284
   103        2:1        1:0      +  *0.728  0.272
   104        2:1        1:0      +  *0.53   0.47 
   105        2:1        2:1          0.14  *0.86 
   106        1:0        1:0         *0.767  0.233
   107        2:1        2:1          0.104 *0.896
   108        2:1        2:1          0.182 *0.818
   109        1:0        2:1      +   0.142 *0.858
   110        1:0        2:1      +   0.299 *0.701
   111        2:1        2:1          0.217 *0.783
   112        2:1        2:1          0.211 *0.789
   113        2:1        2:1          0.209 *0.791
   114        1:0        1:0         *0.937  0.063
   115        2:1        2:1          0.181 *0.819
   116        1:0        1:0         *0.811  0.189
   117        2:1        2:1          0.173 *0.827
   118        1:0        1:0         *0.591  0.409
   119        1:0        1:0         *0.918  0.082
   120        1:0        1:0         *0.555  0.445
   121        1:0        2:1      +   0.433 *0.567
   122        1:0        1:0         *0.682  0.318
   123        1:0        1:0         *0.758  0.242
   124        2:1        2:1          0.433 *0.567
   125        1:0        1:0         *0.53   0.47 
   126        1:0        1:0         *0.784  0.216
   127        2:1        2:1          0.203 *0.797
   128        1:0        1:0         *0.524  0.476
   129        2:1        2:1          0.247 *0.753
   130        1:0        1:0         *0.864  0.136
   131        2:1        2:1          0.346 *0.654
   132        2:1        1:0      +  *0.883  0.117
   133        1:0        2:1      +   0.414 *0.586
   134        1:0        1:0         *0.939  0.061
   135        1:0        1:0         *0.547  0.453
   136        2:1        2:1          0.224 *0.776
   137        1:0        2:1      +   0.369 *0.631
   138        2:1        1:0      +  *0.562  0.438
   139        1:0        1:0         *0.535  0.465
   140        1:0        1:0         *0.822  0.178
   141        1:0        1:0         *0.633  0.367
   142        1:0        1:0         *0.515  0.485
   143        1:0        2:1      +   0.236 *0.764
   144        2:1        1:0      +  *0.639  0.361
   145        1:0        1:0         *0.743  0.257
   146        2:1        2:1          0.06  *0.94 
   147        1:0        1:0         *0.889  0.111
   148        1:0        2:1      +   0.493 *0.507
   149        1:0        1:0         *0.611  0.389
   150        1:0        1:0         *0.523  0.477
   151        1:0        1:0         *0.734  0.266
   152        2:1        2:1          0.365 *0.635
   153        2:1        2:1          0.12  *0.88 
   154        1:0        1:0         *0.501  0.499
   155        2:1        1:0      +  *0.593  0.407
   156        2:1        2:1          0.377 *0.623
   157        2:1        2:1          0.497 *0.503
   158        2:1        2:1          0.068 *0.932
   159        1:0        1:0         *0.629  0.371
   160        1:0        1:0         *0.759  0.241
   161        1:0        1:0         *0.64   0.36 
   162        2:1        2:1          0.446 *0.554
   163        2:1        2:1          0.183 *0.817
   164        1:0        1:0         *0.781  0.219
   165        1:0        2:1      +   0.459 *0.541
   166        1:0        1:0         *0.79   0.21 
   167        2:1        2:1          0.38  *0.62 
   168        1:0        1:0         *0.933  0.067
   169        2:1        2:1          0.463 *0.537
   170        2:1        2:1          0.241 *0.759
   171        2:1        2:1          0.403 *0.597
   172        1:0        1:0         *1      0    
   173        2:1        1:0      +  *0.509  0.491
   174        2:1        1:0      +  *0.518  0.482
   175        2:1        2:1          0.333 *0.667
   176        2:1        2:1          0.429 *0.571
   177        2:1        2:1          0.215 *0.785
   178        2:1        1:0      +  *0.55   0.45 
   179        2:1        2:1          0.301 *0.699
   180        1:0        1:0         *0.807  0.193
   181        2:1        1:0      +  *0.561  0.439
   182        2:1        1:0      +  *0.673  0.327
   183        1:0        1:0         *0.632  0.368
   184        2:1        2:1          0.321 *0.679
   185        2:1        1:0      +  *0.688  0.312
   186        2:1        1:0      +  *0.582  0.418
   187        1:0        1:0         *0.71   0.29 
   188        1:0        1:0         *0.664  0.336
   189        1:0        1:0         *0.508  0.492
   190        2:1        1:0      +  *0.657  0.343
   191        1:0        1:0         *0.902  0.098
   192        2:1        1:0      +  *0.564  0.436
   193        2:1        2:1          0.232 *0.768
   194        2:1        1:0      +  *0.671  0.329
   195        1:0        2:1      +   0.426 *0.574
   196        2:1        1:0      +  *0.568  0.432
   197        1:0        2:1      +   0.406 *0.594
   198        2:1        2:1          0.316 *0.684
   199        2:1        1:0      +  *0.67   0.33 
   200        1:0        2:1      +   0.411 *0.589
   201        2:1        2:1          0.2   *0.8  
   202        2:1        2:1          0.127 *0.873
   203        1:0        1:0         *0.804  0.196
   204        2:1        2:1          0.172 *0.828
   205        1:0        1:0         *0.789  0.211
   206        1:0        1:0         *0.893  0.107
   207        1:0        1:0         *0.824  0.176
   208        2:1        1:0      +  *0.503  0.497
   209        1:0        1:0         *0.651  0.349
   210        1:0        1:0         *0.543  0.457
   211        1:0        2:1      +   0.207 *0.793
   212        1:0        1:0         *0.566  0.434
   213        2:1        2:1          0.142 *0.858
   214        1:0        1:0         *0.609  0.391
   215        2:1        2:1          0.44  *0.56 
   216        1:0        1:0         *0.615  0.385
   217        2:1        2:1          0.337 *0.663
   218        1:0        1:0         *0.967  0.033
   219        1:0        1:0         *0.898  0.102
   220        1:0        1:0         *0.918  0.082
   221        2:1        1:0      +  *0.775  0.225
   222        1:0        2:1      +   0.308 *0.692
   223        1:0        1:0         *0.919  0.081
   224        1:0        2:1      +   0.141 *0.859
   225        2:1        2:1          0.276 *0.724
   226        1:0        1:0         *0.868  0.132
   227        2:1        1:0      +  *0.672  0.328
   228        2:1        1:0      +  *0.655  0.345
   229        1:0        1:0         *0.673  0.327
   230        1:0        1:0         *0.888  0.112
   231        2:1        1:0      +  *0.728  0.272
   232        1:0        2:1      +   0.425 *0.575
   233        1:0        2:1      +   0.49  *0.51 
   234        1:0        1:0         *0.976  0.024
   235        1:0        1:0         *0.78   0.22 
   236        1:0        1:0         *0.855  0.145
   237        2:1        1:0      +  *0.544  0.456
   238        1:0        1:0         *0.907  0.093
   239        1:0        1:0         *0.576  0.424
   240        2:1        2:1          0.156 *0.844
   241        1:0        2:1      +   0.24  *0.76 
   242        2:1        2:1          0.356 *0.644
   243        1:0        1:0         *0.865  0.135
   244        2:1        2:1          0.424 *0.576
   245        2:1        2:1          0.437 *0.563
   246        1:0        1:0         *0.543  0.457
   247        2:1        2:1          0.096 *0.904
   248        1:0        1:0         *0.7    0.3  
   249        1:0        2:1      +   0.375 *0.625
   250        2:1        1:0      +  *0.832  0.168
   251        1:0        1:0         *0.767  0.233
   252        1:0        2:1      +   0.459 *0.541
   253        2:1        2:1          0.061 *0.939
   254        2:1        1:0      +  *0.606  0.394
   255        2:1        2:1          0.344 *0.656
   256        1:0        1:0         *0.674  0.326
   257        1:0        2:1      +   0.448 *0.552
   258        1:0        1:0         *0.52   0.48 
   259        1:0        1:0         *0.78   0.22 
   260        1:0        2:1      +   0.234 *0.766
   261        1:0        1:0         *0.54   0.46 
   262        1:0        1:0         *0.952  0.048
   263        1:0        2:1      +   0.495 *0.505
   264        2:1        2:1          0.386 *0.614
   265        1:0        1:0         *0.848  0.152
   266        2:1        1:0      +  *0.65   0.35 
   267        1:0        1:0         *0.586  0.414
   268        1:0        2:1      +   0.414 *0.586
   269        2:1        2:1          0.153 *0.847
   270        1:0        2:1      +   0.265 *0.735
   271        1:0        1:0         *0.555  0.445
   272        2:1        2:1          0.428 *0.572
   273        2:1        2:1          0.419 *0.581
   274        2:1        1:0      +  *0.513  0.487
   275        1:0        1:0         *0.858  0.142
   276        1:0        1:0         *0.658  0.342
   277        1:0        1:0         *0.624  0.376
   278        2:1        1:0      +  *0.64   0.36 
   279        2:1        1:0      +  *0.516  0.484
   280        1:0        1:0         *0.755  0.245
   281        1:0        1:0         *0.869  0.131
   282        2:1        2:1          0.273 *0.727
   283        1:0        1:0         *0.763  0.237
   284        1:0        1:0         *0.829  0.171
   285        2:1        1:0      +  *0.64   0.36 
   286        2:1        2:1          0.292 *0.708
   287        1:0        1:0         *0.856  0.144
   288        1:0        1:0         *0.854  0.146
   289        2:1        2:1          0.218 *0.782
   290        2:1        2:1          0.355 *0.645
   291        2:1        2:1          0.47  *0.53 
   292        1:0        1:0         *0.857  0.143
   293        2:1        2:1          0.301 *0.699
   294        2:1        1:0      +  *0.716  0.284
   295        2:1        2:1          0.488 *0.512
   296        1:0        1:0         *0.958  0.042
   297        1:0        1:0         *0.851  0.149
   298        1:0        2:1      +   0.441 *0.559
   299        1:0        1:0         *0.763  0.237
   300        2:1        2:1          0.177 *0.823
   301        1:0        1:0         *0.596  0.404
   302        1:0        1:0         *0.836  0.164
   303        2:1        2:1          0.096 *0.904
   304        1:0        1:0         *0.836  0.164
   305        1:0        1:0         *0.778  0.222
   306        1:0        1:0         *0.576  0.424
   307        1:0        1:0         *0.691  0.309
   308        2:1        2:1          0.466 *0.534
   309        1:0        1:0         *0.632  0.368
   310        2:1        1:0      +  *0.749  0.251
   311        1:0        1:0         *0.818  0.182
   312        2:1        2:1          0.404 *0.596
   313        1:0        1:0         *0.776  0.224
   314        1:0        2:1      +   0.311 *0.689
   315        2:1        2:1          0.116 *0.884
   316        1:0        1:0         *0.746  0.254
   317        1:0        1:0         *0.653  0.347
   318        2:1        1:0      +  *0.645  0.355
   319        1:0        1:0         *0.845  0.155
   320        2:1        2:1          0.437 *0.563
   321        2:1        1:0      +  *0.678  0.322
   322        2:1        1:0      +  *0.725  0.275
   323        2:1        1:0      +  *0.523  0.477
   324        2:1        2:1          0.127 *0.873
   325        2:1        1:0      +  *0.737  0.263
   326        1:0        1:0         *0.685  0.315
   327        1:0        1:0         *0.981  0.019
   328        1:0        1:0         *0.718  0.282
   329        1:0        1:0         *0.816  0.184
   330        2:1        2:1          0.422 *0.578
   331        1:0        1:0         *0.536  0.464
   332        1:0        2:1      +   0.38  *0.62 
   333        2:1        2:1          0.094 *0.906
   334        1:0        2:1      +   0.113 *0.887
   335        2:1        2:1          0.186 *0.814
   336        1:0        1:0         *0.772  0.228
   337        1:0        1:0         *0.723  0.277
   338        1:0        1:0         *0.892  0.108
   339        2:1        2:1          0.199 *0.801
   340        2:1        1:0      +  *0.658  0.342
   341        1:0        1:0         *0.8    0.2  
   342        1:0        2:1      +   0.278 *0.722
   343        2:1        1:0      +  *0.612  0.388
   344        2:1        2:1          0.423 *0.577
   345        2:1        2:1          0.094 *0.906
   346        1:0        1:0         *0.574  0.426
   347        2:1        1:0      +  *0.626  0.374
   348        1:0        1:0         *0.678  0.322
   349        1:0        1:0         *0.811  0.189
   350        1:0        2:1      +   0.263 *0.737
   351        1:0        1:0         *0.532  0.468
   352        1:0        1:0         *0.607  0.393
   353        1:0        1:0         *0.809  0.191
   354        1:0        1:0         *0.808  0.192
   355        2:1        2:1          0.434 *0.566
   356        1:0        1:0         *0.709  0.291
   357        1:0        1:0         *0.948  0.052
   358        1:0        1:0         *0.976  0.024
   359        2:1        2:1          0.334 *0.666
   360        2:1        2:1          0.267 *0.733
   361        1:0        1:0         *0.741  0.259
   362        1:0        2:1      +   0.473 *0.527
   363        1:0        2:1      +   0.497 *0.503
   364        1:0        1:0         *0.841  0.159
   365        1:0        1:0         *0.665  0.335
   366        1:0        2:1      +   0.402 *0.598
   367        2:1        2:1          0.315 *0.685
   368        1:0        1:0         *0.677  0.323
   369        1:0        2:1      +   0.4   *0.6  
   370        1:0        2:1      +   0.101 *0.899
   371        1:0        1:0         *0.607  0.393
   372        1:0        2:1      +   0.214 *0.786
   373        1:0        1:0         *0.509  0.491
   374        1:0        1:0         *0.974  0.026
   375        1:0        1:0         *0.935  0.065
   376        1:0        1:0         *0.942  0.058
   377        1:0        1:0         *0.823  0.177
   378        2:1        1:0      +  *0.569  0.431
   379        2:1        1:0      +  *0.91   0.09 
   380        2:1        1:0      +  *0.687  0.313
   381        1:0        1:0         *0.744  0.256
   382        1:0        1:0         *0.51   0.49 
   383        1:0        1:0         *0.697  0.303
   384        2:1        1:0      +  *0.7    0.3  
   385        1:0        1:0         *0.606  0.394
   386        1:0        1:0         *0.635  0.365
   387        2:1        2:1          0.385 *0.615
   388        2:1        2:1          0.309 *0.691
   389        2:1        2:1          0.095 *0.905
   390        1:0        1:0         *0.525  0.475
   391        1:0        1:0         *0.741  0.259
   392        1:0        2:1      +   0.403 *0.597
   393        2:1        1:0      +  *0.575  0.425
   394        1:0        2:1      +   0.366 *0.634
   395        2:1        2:1          0     *1    
   396        2:1        2:1          0.107 *0.893
   397        2:1        1:0      +  *0.605  0.395
   398        1:0        1:0         *0.772  0.228
   399        2:1        1:0      +  *0.794  0.206
   400        2:1        2:1          0.455 *0.545
   401        1:0        1:0         *0.609  0.391
   402        2:1        2:1          0.135 *0.865
   403        2:1        2:1          0.434 *0.566
   404        2:1        2:1          0.246 *0.754
   405        1:0        1:0         *0.566  0.434
   406        2:1        2:1          0.152 *0.848
   407        1:0        1:0         *0.72   0.28 
   408        1:0        2:1      +   0.439 *0.561
   409        1:0        1:0         *0.855  0.145
   410        2:1        1:0      +  *0.617  0.383
   411        1:0        1:0         *0.737  0.263
   412        2:1        1:0      +  *0.771  0.229
   413        1:0        1:0         *0.613  0.387
   414        1:0        1:0         *0.585  0.415
   415        1:0        1:0         *0.969  0.031
   416        1:0        1:0         *0.558  0.442
   417        1:0        1:0         *0.887  0.113
   418        1:0        1:0         *0.56   0.44 
   419        2:1        2:1          0.11  *0.89 
   420        2:1        2:1          0.202 *0.798
   421        2:1        2:1          0.246 *0.754
   422        2:1        2:1          0.244 *0.756
   423        1:0        1:0         *0.531  0.469

=== Evaluation on test split ===
=== Summary ===

Correctly Classified Instances         293               69.2671 %
Incorrectly Classified Instances       130               30.7329 %
Kappa statistic                          0.3728
Mean absolute error                      0.3889
Root mean squared error                  0.4441
Relative absolute error                 78.621  %
Root relative squared error             89.287  %
Total Number of Instances              423     

=== Detailed Accuracy By Class ===

               TP Rate   FP Rate   Precision   Recall  F-Measure   ROC Area  Class
                 0.764     0.395      0.704     0.764     0.733      0.76     0
                 0.605     0.236      0.676     0.605     0.639      0.76     1
Weighted Avg.    0.693     0.323      0.691     0.693     0.69       0.76 

=== Confusion Matrix ===

   a   b   <-- classified as
 178  55 |   a = 0
  75 115 |   b = 1

