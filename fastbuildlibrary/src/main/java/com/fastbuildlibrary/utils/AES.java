package com.fastbuildlibrary.utils;

/**
 * AES 加密算法java实现
 */
public class AES {
    private byte State[][];// 明文的状态表
    private byte Sbox[][];// S盒子
    private byte iSbox[][];// 逆S盒子
    private byte key[];//密钥
    private int Nk;// 密钥长度
    private int Nr;// 对不同的长度的密钥不同的轮数
    private byte w[][];// 密钥调度表
    private byte Rcon[][];// 轮常数表
    private int Nb;// 明文列数


    //构造函数
    public AES(byte[] keyBytes, int keySize) {
        this.Nb = 4;//只处理明文是128位为一组
        if (keySize == 128) {
            this.Nk = 4;
            this.Nr = 10;
        } else if (keySize == 192) {

        //System.out.println("密钥长度位192位");
            this.Nk = 6;
            this.Nr = 12;
        } else if (keySize == 256) {
            this.Nk = 8;
            this.Nr = 14;
        }
        this.key = new byte[this.Nk * 4];
        this.key = keyBytes;
        createSbox();//创建S盒子的方法
        createISbox();//创建S逆盒子的方法
        createRcon();//创建轮常数数组的方法
        keyExtend();//密钥扩展的方法
    }

    // 初始化S盒子
    private void createSbox() {
        this.Sbox = new byte[][]{
                {0x63, 0x7c, 0x77, 0x7b, (byte) 0xf2, 0x6b, 0x6f,
                        (byte) 0xc5, 0x30, 0x01, 0x67, 0x2b, (byte) 0xfe,
                        (byte) 0xd7, (byte) 0xab, 0x76},
                {(byte) 0xca, (byte) 0x82, (byte) 0xc9, 0x7d,
                        (byte) 0xfa, 0x59, 0x47, (byte) 0xf0, (byte) 0xad,
                        (byte) 0xd4, (byte) 0xa2, (byte) 0xaf, (byte) 0x9c,
                        (byte) 0xa4, 0x72, (byte) 0xc0},
                {(byte) 0xb7, (byte) 0xfd, (byte) 0x93, 0x26, 0x36,
                        0x3f, (byte) 0xf7, (byte) 0xcc, 0x34, (byte) 0xa5,
                        (byte) 0xe5, (byte) 0xf1, 0x71, (byte) 0xd8, 0x31, 0x15},
                {0x04, (byte) 0xc7, 0x23, (byte) 0xc3, 0x18,
                        (byte) 0x96, 0x05, (byte) 0x9a, 0x07, 0x12,
                        (byte) 0x80, (byte) 0xe2, (byte) 0xeb, 0x27,
                        (byte) 0xb2, 0x75},
                {0x09, (byte) 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a,
                        (byte) 0xa0, 0x52, 0x3b, (byte) 0xd6, (byte) 0xb3,
                        0x29, (byte) 0xe3, 0x2f, (byte) 0x84},
                {0x53, (byte) 0xd1, 0x00, (byte) 0xed, 0x20,
                        (byte) 0xfc, (byte) 0xb1, 0x5b, 0x6a, (byte) 0xcb,
                        (byte) 0xbe, 0x39, 0x4a, 0x4c, 0x58, (byte) 0xcf},
                {(byte) 0xd0, (byte) 0xef, (byte) 0xaa, (byte) 0xfb,
                        0x43, 0x4d, 0x33, (byte) 0x85, 0x45, (byte) 0xf9, 0x02,
                        0x7f, 0x50, 0x3c, (byte) 0x9f, (byte) 0xa8},
                {0x51, (byte) 0xa3, 0x40, (byte) 0x8f, (byte) 0x92,
                        (byte) 0x9d, 0x38, (byte) 0xf5, (byte) 0xbc,
                        (byte) 0xb6, (byte) 0xda, 0x21, 0x10, (byte) 0xff,
                        (byte) 0xf3, (byte) 0xd2},
                {(byte) 0xcd, 0x0c, 0x13, (byte) 0xec, 0x5f,
                        (byte) 0x97, 0x44, 0x17, (byte) 0xc4, (byte) 0xa7,
                        0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
                {0x60, (byte) 0x81, 0x4f, (byte) 0xdc, 0x22, 0x2a,
                        (byte) 0x90, (byte) 0x88, 0x46, (byte) 0xee,
                        (byte) 0xb8, 0x14, (byte) 0xde, 0x5e, 0x0b, (byte) 0xdb},
                {(byte) 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c,
                        (byte) 0xc2, (byte) 0xd3, (byte) 0xac, 0x62,
                        (byte) 0x91, (byte) 0x95, (byte) 0xe4, 0x79},
                {(byte) 0xe7, (byte) 0xc8, 0x37, 0x6d, (byte) 0x8d,
                        (byte) 0xd5, 0x4e, (byte) 0xa9, 0x6c, 0x56,
                        (byte) 0xf4, (byte) 0xea, 0x65, 0x7a, (byte) 0xae, 0x08},
                {(byte) 0xba, 0x78, 0x25, 0x2e, 0x1c, (byte) 0xa6,
                        (byte) 0xb4, (byte) 0xc6, (byte) 0xe8, (byte) 0xdd,
                        0x74, 0x1f, 0x4b, (byte) 0xbd, (byte) 0x8b, (byte) 0x8a},
                {0x70, 0x3e, (byte) 0xb5, 0x66, 0x48, 0x03,
                        (byte) 0xf6, 0x0e, 0x61, 0x35, 0x57, (byte) 0xb9,
                        (byte) 0x86, (byte) 0xc1, 0x1d, (byte) 0x9e},
                {(byte) 0xe1, (byte) 0xf8, (byte) 0x98, 0x11, 0x69,
                        (byte) 0xd9, (byte) 0x8e, (byte) 0x94, (byte) 0x9b,
                        0x1e, (byte) 0x87, (byte) 0xe9, (byte) 0xce, 0x55,
                        0x28, (byte) 0xdf},
                {(byte) 0x8c, (byte) 0xa1, (byte) 0x89, 0x0d,
                        (byte) 0xbf, (byte) 0xe6, 0x42, 0x68, 0x41,
                        (byte) 0x99, 0x2d, 0x0f, (byte) 0xb0, 0x54,
                        (byte) 0xbb, 0x16}
        };


    }


    /*生成S逆盒子：
    *我的思路：得到s盒中一个元素的高四位和低四位，分别作为s逆盒子的行和列。
    *然后在把s盒中的行和列转换成十六进制存到s逆盒中。
    * */
    private void createISbox() {
        int x, y;
        this.iSbox = new byte[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
// 得到s盒子元素中的高四位的值
                x = (int) ((this.Sbox[i][j] >> 4) & 0x0f);
// 得到s盒子元素中的低四位的值
                y = (int) (this.Sbox[i][j] & 0x0f);
//高四位和低四位分别做为s逆盒子的行和列，将s盒子的行和列变成十六进制放到s逆盒子中
                this.iSbox[x][y] = (byte) (16 * i + j);
            }


        }


    }

    //初始化Rcon轮常数二维数组
    private void createRcon() {
        this.Rcon = new byte[][]{
                {0x00, 0x00, 0x00, 0x00},
                {0x01, 0x00, 0x00, 0x00},
                {0x02, 0x00, 0x00, 0x00},
                {0x04, 0x00, 0x00, 0x00},
                {0x08, 0x00, 0x00, 0x00},
                {0x10, 0x00, 0x00, 0x00},
                {0x20, 0x00, 0x00, 0x00},
                {0x40, 0x00, 0x00, 0x00},
                {(byte) 0x80, 0x00, 0x00, 0x00},
                {0x1B, 0x00, 0x0, 0x00},
                {0x36, 0x00, 0x00, 0x00},
        };
    }

    /*
    * RotWord方法:将密钥w[i-1]的四个字节循环左移位，
    * 返回移位后的结果。
    * */
    private byte[] RotWord(byte[] rotWord) {
        byte[] b = new byte[4];
        b[0] = rotWord[1];
        b[1] = rotWord[2];
        b[2] = rotWord[3];
        b[3] = rotWord[0];
        return b;
    }

    /* SubWord方法：将密钥w[i-1]的四个字节分别用S盒子的内容进行替换
    * 返回替换后的结果。
    * */
    private byte[] SubWord(byte[] subWord) {
        for (int i = 0; i < subWord.length; i++) {
            subWord[i] = this.Sbox[(subWord[i] >> 4 & 0x0f)][(subWord[i] & 0x0f)];
        }
        return subWord;
    }

    /*
    * 扩展密钥的方法
    * */
    private void keyExtend() {
        this.w = new byte[Nb * (Nr + 1)][4];

        for (int i = 0; i < Nk; i++) {
            this.w[i][0] = this.key[4 * i];
            this.w[i][1] = this.key[4 * i + 1];
            this.w[i][2] = this.key[4 * i + 2];
            this.w[i][3] = this.key[4 * i + 3];
//对轮密钥十六进制输出
/*System.out.print("w["+i+"]:");
for(int j=0;j<=3;j++){
System.out.format("%x  ",w[i][j]);
}
System.out.println("");*/
        }

        byte[] temp = new byte[4];
        for (int i = Nk; i < Nb * (Nr + 1); i++) {
            temp[0] = w[i - 1][0];
            temp[1] = w[i - 1][1];
            temp[2] = w[i - 1][2];
            temp[3] = w[i - 1][3];
            if (i % Nk == 0) {
                temp = SubWord(RotWord(temp));
                temp[0] = (byte) (temp[0] ^ Rcon[i / Nk][0]);
                temp[1] = (byte) (temp[1] ^ Rcon[i / Nk][1]);
                temp[2] = (byte) (temp[2] ^ Rcon[i / Nk][2]);
                temp[3] = (byte) (temp[3] ^ Rcon[i / Nk][3]);


            } else if (Nk > 6 && (i % Nk == 4)) {
                temp = SubWord(temp);
            }
            this.w[i][0] = (byte) (this.w[i - Nk][0] ^ temp[0]);
            this.w[i][1] = (byte) (this.w[i - Nk][1] ^ temp[1]);
            this.w[i][2] = (byte) (this.w[i - Nk][2] ^ temp[2]);
            this.w[i][3] = (byte) (this.w[i - Nk][3] ^ temp[3]);
//对轮密钥十六进制输出
/*System.out.print("w["+i+"]:");
for(int j=0;j<=3;j++){
System.out.format("%x  ",w[i][j]);
}
System.out.println("");*/
        }
        this.setW(this.w);
    }

    //byte数组转换成String输出。即到时候在前台输出密钥扩展的内容
    public static String BytesToString(String w, byte[] b) {
        String s = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex + "  ";
            } else {
                hex = hex + "  ";
            }
            s += hex;
        }
        s = w + " " + s;
        return s;
    }

    /*
    * 以下是加密过程的每个方法
    *
    * */
// 轮加密
    private void addRoundKey(int round) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < Nb; c++) {
                this.State[r][c] = (byte) (this.State[r][c] ^ w[round * Nb + c][r]);
            }
        }
    }

    // 字节替换
    private void subByte() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < Nb; c++) {
                this.State[r][c] = this.Sbox[(this.State[r][c] >> 4) & 0x0f][(this.State[r][c] & 0x0f)];
            }
        }
    }

    // 逆字节替换
    private void invSubByte() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < Nb; c++) {
                this.State[r][c] = this.iSbox[(this.State[r][c] >> 4) & 0x0f][(this.State[r][c] & 0x0f)];
            }
        }
    }

    // 行移位
    private void shiftRow() {
        byte[][] temp = new byte[4][Nb];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < Nb; c++) {
                temp[r][c] = this.State[r][c];
            }
        }
// 开始移位
        for (int r = 1; r < 4; r++) {
            for (int c = 0; c < Nb; c++) {
                this.State[r][c] = temp[r][(r + c) % Nb];
            }
        }
    }

    // 逆行移位
    private void invShiftRow() {
        byte[][] temp = new byte[4][Nb];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < Nb; c++) {
                temp[r][c] = this.State[r][c];
            }
        }
// 开始移位
        for (int r = 1; r < 4; r++) {
            for (int c = 0; c < Nb; c++) {
                this.State[r][c] = temp[r][(c - r + Nb) % Nb];
            }
        }
    }

    // 列混合
    private void mixColum() {
        byte[][] temp = new byte[4][Nb];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < Nb; c++) {
                temp[r][c] = this.State[r][c];
            }
        }
        for (int c = 0; c < Nb; c++) {
            this.State[0][c] = (byte) (mult2(temp[0][c])
                    ^ multBy03(temp[1][c]) ^ mult1(temp[2][c]) ^ mult1(temp[3][c]));
            this.State[1][c] = (byte) (mult1(temp[0][c])
                    ^ mult2(temp[1][c]) ^ multBy03(temp[2][c]) ^ mult1(temp[3][c]));
            this.State[2][c] = (byte) (mult1(temp[0][c])
                    ^ mult1(temp[1][c]) ^ mult2(temp[2][c]) ^ multBy03(temp[3][c]));
            this.State[3][c] = (byte) (multBy03(temp[0][c])
                    ^ mult1(temp[1][c]) ^ mult1(temp[2][c]) ^ mult2(temp[3][c]));
        }
    }

    //逆列混合
    private void invMixColum() {
        byte[][] temp = new byte[4][Nb];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < Nb; c++) {
                temp[r][c] = this.State[r][c];
            }
        }
        for (int c = 0; c < Nb; c++) {
            this.State[0][c] = (byte) (multBy0e(temp[0][c])
                    ^ multBy0b(temp[1][c]) ^ multBy0d(temp[2][c]) ^ multBy09(temp[3][c]));
            this.State[1][c] = (byte) (multBy09(temp[0][c])
                    ^ multBy0e(temp[1][c]) ^ multBy0b(temp[2][c]) ^ multBy0d(temp[3][c]));
            this.State[2][c] = (byte) (multBy0d(temp[0][c])
                    ^ multBy09(temp[1][c]) ^ multBy0e(temp[2][c]) ^ multBy0b(temp[3][c]));
            this.State[3][c] = (byte) (multBy0b(temp[0][c])
                    ^ multBy0d(temp[1][c]) ^ multBy09(temp[2][c]) ^ multBy0e(temp[3][c]));
        }
    }


    //字节*01
    private byte mult1(byte b) {
        return b;
    }

    // 字节*02的结果
    private byte mult2(byte b) {
        if ((b >> 7 & 0x01) == 0) {// 若字节最高位为0，则就是将b左移1位
            return (byte) (b << 1);
        } else {
            byte c = 0x1b;
            return (byte) ((b << 1) ^ c);
        }
    }


    // 字节*03的结果,相当于b*（02+01）。后面的方法类似
    private byte multBy03(byte b) {
        return (byte) (mult2(b) ^ mult1(b));
    }

    // 字节b*09的结果
    private byte multBy09(byte b) {
        return (byte) (mult2(mult2(mult2(b))) ^ mult1(b));
    }

    // 字节*0b的结果
    private byte multBy0b(byte b) {
        return (byte) (mult2(mult2(mult2(b))) ^ mult2(b) ^ mult1(b));
    }

    // 字节*0d的结果
    private byte multBy0d(byte b) {
        return (byte) (mult2(mult2(mult2(b))) ^ mult2(mult2(b)) ^ mult1(b));
    }

    // 字节*0e的结果
    private byte multBy0e(byte b) {
        return (byte) (mult2(mult2(mult2(b))) ^ mult2(mult2(b)) ^ mult2(b));
    }

    /*
    * 1、整个加密过程
    * 2、传入的参数是明文和密文的字节数组
    * */
    public void encrypt(byte[] input, byte[] output) {

//初始化状态列
        this.State = new byte[4][Nb];
        for (int i = 0; i < (4 * Nb); i++) {
            this.State[(i / Nb)][(i % Nb)] = input[i];
        }

        addRoundKey(0);//开始轮密钥加密1次
        for (int round = 1; round < Nr; round++) {//中间加密Nr-1次加密
            subByte();//字节替换
            shiftRow();//行移位
            mixColum();//列混合
            addRoundKey(round);//加密
        }
        subByte();//字节替换
        shiftRow();//行移位
        addRoundKey(Nr);//加密

//把加密后的状态放入到输出的密文中
        for (int i = 0; i < (4 * Nb); i++) {
            output[i] = this.State[(i / Nb)][(i % Nb)];
        }
    }


    /*
    * 1、整个解密过程
    * 2、参数分别是密钥字节数组和解密后明文的字节数组
    *
    * */
    public void decrypt(byte[] input, byte[] output) {

//初始化状态列
        this.State = new byte[4][Nb];
        for (int i = 0; i < (4 * Nb); i++) {
            this.State[(i / Nb)][(i % Nb)] = input[i];
//System.out.format("%x,",this.State[(i/Nb)][(i%Nb)]);
        }

        addRoundKey(Nr);//用最后一轮密钥加密
        for (int round = Nr - 1; round > 0; round--) {
            invShiftRow();//逆行移位
            invSubByte();//逆字节替换
            addRoundKey(round);//轮密钥加密
            invMixColum();//逆列混合
        }
        invShiftRow();//逆行移位
        invSubByte();//逆字节替换
        addRoundKey(0);//用第0轮的密钥加密

        for (int i = 0; i < (4 * Nb); i++) {
            output[i] = this.State[(i / Nb)][(i % Nb)];
        }

    }

    //设置密钥
    public byte[][] getW() {
        return w;
    }

    public void setW(byte[][] w) {
        this.w = w;
    }
}
