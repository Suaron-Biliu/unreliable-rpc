package org.suaron.cahce;

import org.suaron.core.UnreliableInvocation;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CustomerCache {

    public static BlockingQueue<UnreliableInvocation> SEND_QUEUE = new ArrayBlockingQueue(100);
    public static Map<String,Object> RESP_MAP = new ConcurrentHashMap<>();
}
