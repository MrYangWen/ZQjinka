package com.ximei.tiny.tools;


enum AESType {
    AES_128(128), AES_192(192), AES_256(256);
    public int value;
    private AESType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
}
