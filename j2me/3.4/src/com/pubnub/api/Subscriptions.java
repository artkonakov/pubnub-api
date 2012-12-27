package com.pubnub.api;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Pubnub
 *
 */
class Subscriptions {
    private Hashtable channels;

    public Subscriptions() {
        channels = new Hashtable();
    }

    public void addChannel(Channel channel) {
        channels.put(channel.name, channel);
    }

    public void removeChannel(String name) {
        channels.remove(name);
    }

    public Channel getFirstChannel() {
        return (Channel) channels.elements().nextElement();

    }

    public Channel getChannel(String name) {
        return (Channel) channels.get(name);
    }

    public String[] getChannelNames() {

        return PubnubUtil.hashtableKeysToArray(channels);
    }

    public String getChannelString() {
        return PubnubUtil.hashTableKeysToDelimitedString(channels, ",");

    }

    public void invokeConnectCallbackOnChannels() {
    	System.out.println("Call connect callbacks");
        /*
         * Iterate over all the channels and call connect callback for channels
         * which were in disconnected state. Nothing to do for channels already
         * connected
         */
        synchronized (channels) {
            Enumeration ch = channels.elements();
            while (ch.hasMoreElements()) {
                Channel _channel = (Channel) ch.nextElement();
                if (_channel.connected == false) {
                    _channel.connected = true;
                    if (_channel.subscribed == false) {
                    	_channel.callback.connectCallback(_channel.name);
                    } else {
                    	_channel.subscribed = true;
                    	_channel.callback.reconnectCallback(_channel.name);
                    }
                }
            }
        }
    }

    public void invokeDisconnectCallbackOnChannels() {
    	System.out.println("Call disconnect callbacks");
        /*
         * Iterate over all the channels and call connect callback for channels
         * which were in disconnected state. Nothing to do for channels already
         * connected
         */
        synchronized (channels) {
            Enumeration ch = channels.elements();
            while (ch.hasMoreElements()) {
                Channel _channel = (Channel) ch.nextElement();
                if (_channel.connected == true) {
                    _channel.connected = false;
                    _channel.callback.disconnectCallback(_channel.name);
                }
            }
        }
    }

    public void invokeConnectCallbackOnChannels(String[] channels) {
    	for (int i = 0; i < channels.length; i++) {
    		Channel _channel = (Channel)this.channels.get(channels[i]);
    		if (_channel.connected == false) {
                _channel.connected = true;
                if (_channel.subscribed == false) {
                	_channel.callback.connectCallback(_channel.name);
                } else {
                	_channel.subscribed = true;
                	_channel.callback.reconnectCallback(_channel.name);
                }
            }
    	}
    }

    public void invokeDisconnectCallbackOnChannels(String[] channels) {
    	for (int i = 0; i < channels.length; i++) {
    		Channel _channel = (Channel)this.channels.get(channels[i]);
    		if (_channel.connected == true) {
                _channel.connected = false;
                _channel.callback.disconnectCallback(_channel.name);
            }
    	}
    }
}
