package com.github.rpc.codec;

import java.util.EnumMap;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by qianxuecheng on 15/9/3.
 */
public class CodecFactory {
    public  static enum CodecType{
        Jdk;
        public static CodecType getCodecType(int codecType){
            return CodecType.values()[codecType];

        }
    }
    public static final EnumMap<CodecType,Pair<Encoder, Decoder>> codecType2Pair;
    static {
        codecType2Pair=new EnumMap<CodecType, Pair<Encoder, Decoder>>(CodecType.class);
        codecType2Pair.put(CodecType.Jdk, Pair.<Encoder,Decoder>of(new JdkEncoder(),new JdkDecoder()));
    }

    public static  Encoder getEncoder(CodecType codecType){
        return codecType2Pair.get(codecType).getLeft();
    }

    public static Decoder getDecoder(CodecType codecType){
        return codecType2Pair.get(codecType).getRight();
    }


}
