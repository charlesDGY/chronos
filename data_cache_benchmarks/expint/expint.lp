Maximize

135 dSta_0 + 31 d0_19 + 1 d1_2.v0 + 1 d1_2.v1 + 2 d1_3.v0 + 2 d1_3.v1 + 2 d2_3.v0 + 2 d2_3.v1 + 2 d3_4.v0 + 2 d3_4.v1 + 7 d3_6.v0 + 7 d3_6.v1 + 1 d4_5.v0 + 1 d4_5.v1 + 7 d4_6.v0 + 7 d4_6.v1 + 7 d5_6.v0 + 7 d5_6.v1 + 7 d6_7.v0 + 7 d6_7.v1 + 3 d6_13.v0 + 3 d6_13.v1 + 1 d7_8.v0 + 1 d7_8.v1 + 2 d7_9.v0 + 2 d7_9.v1 + 2 d8_9.v0 + 2 d8_9.v1 + 2 d9_10.v0 + 2 d9_10.v1 + 2 d9_12.v0 + 2 d9_12.v1 + 1 d10_11.v0 + 1 d10_11.v1 + 2 d10_12.v0 + 2 d10_12.v1 + 2 d11_12.v0 + 2 d11_12.v1 + 4 d12_16.v0 + 4 d12_16.v1 + 6 d13_15.v0 + 6 d13_15.v1 + 5 d13_18.v0 + 5 d13_18.v1 + 6 d14_15.v00 + 6 d14_15.v01 + 6 d14_15.v10 + 6 d14_15.v11 + 5 d14_14.v00 + 5 d14_14.v01 + 5 d14_14.v10 + 5 d14_14.v11 + 4 d15_16.v0 + 4 d15_16.v1 + 2 d16_17.v0 + 2 d16_17.v1 + 5 d16_1.v0 + 5 d16_1.v1 + 5 d18_14.v0 + 5 d18_14.v1 + 6 d18_15.v0 + 6 d18_15.v1 + 1 d19_20 + 2 d19_21 + 2 d20_21 + 32 d21_22 + 37 d21_24 + 1 d22_23 + 36 d22_24 + 36 d23_24 + 36 d24_25 + 63 d24_31 + 1 d25_26 + 32 d25_27 + 32 d26_27 + 2 d27_28 + 2 d27_30 + 30 d28_29 + 2 d28_30 + 2 d29_30 + 33 d30_34 + 33 d31_33 + 34 d31_35 + 32 d32_33.v0 + 32 d32_33.v1 + 5 d32_32.v0 + 5 d32_32.v1 + 33 d33_34 + 5 d34_1 + 2 d34_17 + 5 d35_32 + 32 d35_33


Subject to


\ === tcfg constraints ===
dSta_0 = 1
b0 - d0_19 = 0
b0 - dSta_0 = 0
b1 - d1_2 - d1_3 = 0
b1 - d16_1 - d34_1 = 0
b2 - d2_3 = 0
b2 - d1_2 = 0
b3 - d3_4 - d3_6 = 0
b3 - d1_3 - d2_3 = 0
b4 - d4_5 - d4_6 = 0
b4 - d3_4 = 0
b5 - d5_6 = 0
b5 - d4_5 = 0
b6 - d6_7 - d6_13 = 0
b6 - d3_6 - d4_6 - d5_6 = 0
b7 - d7_8 - d7_9 = 0
b7 - d6_7 = 0
b8 - d8_9 = 0
b8 - d7_8 = 0
b9 - d9_10 - d9_12 = 0
b9 - d7_9 - d8_9 = 0
b10 - d10_11 - d10_12 = 0
b10 - d9_10 = 0
b11 - d11_12 = 0
b11 - d10_11 = 0
b12 - d12_16 = 0
b12 - d9_12 - d10_12 - d11_12 = 0
b13 - d13_15 - d13_18 = 0
b13 - d6_13 = 0
b14 - d14_15 - d14_14 = 0
b14 - d14_14 - d18_14 = 0
b15 - d15_16 = 0
b15 - d13_15 - d14_15 - d18_15 = 0
b16 - d16_17 - d16_1 = 0
b16 - d12_16 - d15_16 = 0
b17 - d16_17 - d34_17 = 0
b18 - d18_14 - d18_15 = 0
b18 - d13_18 = 0
b19 - d19_20 - d19_21 = 0
b19 - d0_19 = 0
b20 - d20_21 = 0
b20 - d19_20 = 0
b21 - d21_22 - d21_24 = 0
b21 - d19_21 - d20_21 = 0
b22 - d22_23 - d22_24 = 0
b22 - d21_22 = 0
b23 - d23_24 = 0
b23 - d22_23 = 0
b24 - d24_25 - d24_31 = 0
b24 - d21_24 - d22_24 - d23_24 = 0
b25 - d25_26 - d25_27 = 0
b25 - d24_25 = 0
b26 - d26_27 = 0
b26 - d25_26 = 0
b27 - d27_28 - d27_30 = 0
b27 - d25_27 - d26_27 = 0
b28 - d28_29 - d28_30 = 0
b28 - d27_28 = 0
b29 - d29_30 = 0
b29 - d28_29 = 0
b30 - d30_34 = 0
b30 - d27_30 - d28_30 - d29_30 = 0
b31 - d31_33 - d31_35 = 0
b31 - d24_31 = 0
b32 - d32_33 - d32_32 = 0
b32 - d32_32 - d35_32 = 0
b33 - d33_34 = 0
b33 - d31_33 - d32_33 - d35_33 = 0
b34 - d34_1 - d34_17 = 0
b34 - d30_34 - d33_34 = 0
b35 - d35_32 - d35_33 = 0
b35 - d31_35 = 0


\ === context constraints ===

\(a)
d1_2 - d1_2.v0 - d1_2.v1 = 0
d1_3 - d1_3.v0 - d1_3.v1 = 0
d2_3 - d2_3.v0 - d2_3.v1 = 0
d3_4 - d3_4.v0 - d3_4.v1 = 0
d3_6 - d3_6.v0 - d3_6.v1 = 0
d4_5 - d4_5.v0 - d4_5.v1 = 0
d4_6 - d4_6.v0 - d4_6.v1 = 0
d5_6 - d5_6.v0 - d5_6.v1 = 0
d6_7 - d6_7.v0 - d6_7.v1 = 0
d6_13 - d6_13.v0 - d6_13.v1 = 0
d7_8 - d7_8.v0 - d7_8.v1 = 0
d7_9 - d7_9.v0 - d7_9.v1 = 0
d8_9 - d8_9.v0 - d8_9.v1 = 0
d9_10 - d9_10.v0 - d9_10.v1 = 0
d9_12 - d9_12.v0 - d9_12.v1 = 0
d10_11 - d10_11.v0 - d10_11.v1 = 0
d10_12 - d10_12.v0 - d10_12.v1 = 0
d11_12 - d11_12.v0 - d11_12.v1 = 0
d12_16 - d12_16.v0 - d12_16.v1 = 0
d13_15 - d13_15.v0 - d13_15.v1 = 0
d13_18 - d13_18.v0 - d13_18.v1 = 0
d14_15 - d14_15.v00 - d14_15.v01 - d14_15.v10 - d14_15.v11 = 0
d14_14 - d14_14.v00 - d14_14.v01 - d14_14.v10 - d14_14.v11 = 0
d15_16 - d15_16.v0 - d15_16.v1 = 0
d16_17 - d16_17.v0 - d16_17.v1 = 0
d16_1 - d16_1.v0 - d16_1.v1 = 0
d18_14 - d18_14.v0 - d18_14.v1 = 0
d18_15 - d18_15.v0 - d18_15.v1 = 0
d32_33 - d32_33.v0 - d32_33.v1 = 0
d32_32 - d32_32.v0 - d32_32.v1 = 0

\(b)
b1.v0 - d1_2.v0 - d1_3.v0 = 0
b1.v1 - d1_2.v1 - d1_3.v1 = 0
b2.v0 - d2_3.v0 = 0
b2.v1 - d2_3.v1 = 0
b3.v0 - d3_4.v0 - d3_6.v0 = 0
b3.v1 - d3_4.v1 - d3_6.v1 = 0
b4.v0 - d4_5.v0 - d4_6.v0 = 0
b4.v1 - d4_5.v1 - d4_6.v1 = 0
b5.v0 - d5_6.v0 = 0
b5.v1 - d5_6.v1 = 0
b6.v0 - d6_7.v0 - d6_13.v0 = 0
b6.v1 - d6_7.v1 - d6_13.v1 = 0
b7.v0 - d7_8.v0 - d7_9.v0 = 0
b7.v1 - d7_8.v1 - d7_9.v1 = 0
b8.v0 - d8_9.v0 = 0
b8.v1 - d8_9.v1 = 0
b9.v0 - d9_10.v0 - d9_12.v0 = 0
b9.v1 - d9_10.v1 - d9_12.v1 = 0
b10.v0 - d10_11.v0 - d10_12.v0 = 0
b10.v1 - d10_11.v1 - d10_12.v1 = 0
b11.v0 - d11_12.v0 = 0
b11.v1 - d11_12.v1 = 0
b12.v0 - d12_16.v0 = 0
b12.v1 - d12_16.v1 = 0
b13.v0 - d13_15.v0 - d13_18.v0 = 0
b13.v1 - d13_15.v1 - d13_18.v1 = 0
b14.v00 - d14_15.v00 - d14_14.v00 = 0
b14.v01 - d14_15.v01 - d14_14.v01 = 0
b14.v10 - d14_15.v10 - d14_14.v10 = 0
b14.v11 - d14_15.v11 - d14_14.v11 = 0
b15.v0 - d15_16.v0 = 0
b15.v1 - d15_16.v1 = 0
b16.v0 - d16_17.v0 - d16_1.v0 = 0
b16.v1 - d16_17.v1 - d16_1.v1 = 0
b18.v0 - d18_14.v0 - d18_15.v0 = 0
b18.v1 - d18_14.v1 - d18_15.v1 = 0
b32.v0 - d32_33.v0 - d32_32.v0 = 0
b32.v1 - d32_33.v1 - d32_32.v1 = 0

\(c)
b1 - b1.v0 - b1.v1 = 0
b2 - b2.v0 - b2.v1 = 0
b3 - b3.v0 - b3.v1 = 0
b4 - b4.v0 - b4.v1 = 0
b5 - b5.v0 - b5.v1 = 0
b6 - b6.v0 - b6.v1 = 0
b7 - b7.v0 - b7.v1 = 0
b8 - b8.v0 - b8.v1 = 0
b9 - b9.v0 - b9.v1 = 0
b10 - b10.v0 - b10.v1 = 0
b11 - b11.v0 - b11.v1 = 0
b12 - b12.v0 - b12.v1 = 0
b13 - b13.v0 - b13.v1 = 0
b14 - b14.v00 - b14.v01 - b14.v10 - b14.v11 = 0
b15 - b15.v0 - b15.v1 = 0
b16 - b16.v0 - b16.v1 = 0
b18 - b18.v0 - b18.v1 = 0
b32 - b32.v0 - b32.v1 = 0

\(d)
b14.v00 - d18_14.v0 = 0
b14.v10 - d18_14.v1 = 0
b1.v0 - d34_1 = 0
b32.v0 - d35_32 = 0

\(e)
b14.v01 - d14_14.v00 - d14_14.v01 = 0
b14.v01 - 0 d14_14.v00 <= 0
b14.v11 - d14_14.v10 - d14_14.v11 = 0
b14.v11 - 0 d14_14.v10 <= 0
b1.v1 - d16_1.v0 - d16_1.v1 = 0
b1.v1 - 98 d16_1.v0 <= 0
b32.v1 - d32_32.v0 - d32_32.v1 = 0
b32.v1 - 0 d32_32.v0 <= 0

\(f)
d14_15.v0 - d14_15.v00 - d14_15.v01 = 0
d14_15.v1 - d14_15.v10 - d14_15.v11 = 0
d16_17 - d16_17.v0 - d16_17.v1 = 0
d32_33 - d32_33.v0 - d32_33.v1 = 0

\(g)
b2.v0 - d1_2.v0 = 0
b2.v1 - d1_2.v1 = 0
b3.v0 - d1_3.v0 - d2_3.v0 = 0
b3.v1 - d1_3.v1 - d2_3.v1 = 0
b4.v0 - d3_4.v0 = 0
b4.v1 - d3_4.v1 = 0
b5.v0 - d4_5.v0 = 0
b5.v1 - d4_5.v1 = 0
b6.v0 - d3_6.v0 - d4_6.v0 - d5_6.v0 = 0
b6.v1 - d3_6.v1 - d4_6.v1 - d5_6.v1 = 0
b7.v0 - d6_7.v0 = 0
b7.v1 - d6_7.v1 = 0
b8.v0 - d7_8.v0 = 0
b8.v1 - d7_8.v1 = 0
b9.v0 - d7_9.v0 - d8_9.v0 = 0
b9.v1 - d7_9.v1 - d8_9.v1 = 0
b10.v0 - d9_10.v0 = 0
b10.v1 - d9_10.v1 = 0
b11.v0 - d10_11.v0 = 0
b11.v1 - d10_11.v1 = 0
b12.v0 - d9_12.v0 - d10_12.v0 - d11_12.v0 = 0
b12.v1 - d9_12.v1 - d10_12.v1 - d11_12.v1 = 0
b13.v0 - d6_13.v0 = 0
b13.v1 - d6_13.v1 = 0
b15.v0 - d13_15.v0 - d14_15.v0 - d18_15.v0 = 0
b15.v1 - d13_15.v1 - d14_15.v1 - d18_15.v1 = 0
b16.v0 - d12_16.v0 - d15_16.v0 = 0
b16.v1 - d12_16.v1 - d15_16.v1 = 0
b18.v0 - d13_18.v0 = 0
b18.v1 - d13_18.v1 = 0

\ === header constraints ===
b0 <= 1
b14 - 1 b1 <= 0
b14 - 1 d18_14 <= 0
b1 - 99 b0 <= 0
b1 - 99 d34_1 <= 0
b32 - 1 b0 <= 0
b32 - 1 d35_32 <= 0

Generals

Sta
b0
d0_19
b1
d1_2
d1_3
b2
d2_3
b3
d3_4
d3_6
b4
d4_5
d4_6
b5
d5_6
b6
d6_7
d6_13
b7
d7_8
d7_9
b8
d8_9
b9
d9_10
d9_12
b10
d10_11
d10_12
b11
d11_12
b12
d12_16
b13
d13_15
d13_18
b14
d14_15
d14_14
b15
d15_16
b16
d16_17
d16_1
b17
b18
d18_14
d18_15
b19
d19_20
d19_21
b20
d20_21
b21
d21_22
d21_24
b22
d22_23
d22_24
b23
d23_24
b24
d24_25
d24_31
b25
d25_26
d25_27
b26
d26_27
b27
d27_28
d27_30
b28
d28_29
d28_30
b29
d29_30
b30
d30_34
b31
d31_33
d31_35
b32
d32_33
d32_32
b33
d33_34
b34
d34_1
d34_17
b35
d35_32
d35_33
d0_19
d1_2.v0
d1_2.v1
d1_3.v0
d1_3.v1
d2_3.v0
d2_3.v1
d3_4.v0
d3_4.v1
d3_6.v0
d3_6.v1
d4_5.v0
d4_5.v1
d4_6.v0
d4_6.v1
d5_6.v0
d5_6.v1
d6_7.v0
d6_7.v1
d6_13.v0
d6_13.v1
d7_8.v0
d7_8.v1
d7_9.v0
d7_9.v1
d8_9.v0
d8_9.v1
d9_10.v0
d9_10.v1
d9_12.v0
d9_12.v1
d10_11.v0
d10_11.v1
d10_12.v0
d10_12.v1
d11_12.v0
d11_12.v1
d12_16.v0
d12_16.v1
d13_15.v0
d13_15.v1
d13_18.v0
d13_18.v1
d14_15.v00
d14_15.v01
d14_15.v10
d14_15.v11
d14_14.v00
d14_14.v01
d14_14.v10
d14_14.v11
d15_16.v0
d15_16.v1
d16_17.v0
d16_17.v1
d16_1.v0
d16_1.v1
d18_14.v0
d18_14.v1
d18_15.v0
d18_15.v1
d19_20
d19_21
d20_21
d21_22
d21_24
d22_23
d22_24
d23_24
d24_25
d24_31
d25_26
d25_27
d26_27
d27_28
d27_30
d28_29
d28_30
d29_30
d30_34
d31_33
d31_35
d32_33.v0
d32_33.v1
d32_32.v0
d32_32.v1
d33_34
d34_1
d34_17
d35_32
d35_33

End
\ total_cons: 210 	total_vars: 94
