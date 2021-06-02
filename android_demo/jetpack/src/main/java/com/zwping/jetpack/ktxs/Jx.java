package com.zwping.jetpack.ktxs;

/**
 * 实现ktx高阶函数特性
 * zwping @ 5/18/21
 */
public class Jx {

    public interface OnCommCallback<T> { void callback(T it); }

    public static <T> T also(T it, OnCommCallback<T> callback) { callback.callback(it); return it; }

}
