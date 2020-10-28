package me.glaremasters.rocketeer.messages;

import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;

/**
 * Created by Glare
 * Date: 10/27/2020
 * Time: 7:13 PM
 */
public enum Messages implements MessageKeyProvider {
	CREATE__SUCCESS,
	CREATE__ERROR,

	GIVE__SUCCESS,

	SAVE__SUCCESS,
	SAVE__ALREADY_EXISTS,

	LIST__DISPLAY,

	REMOVE__SUCCESS,

	HEIGHT_SUCCESS;

	private final MessageKey key = MessageKey.of(name().toLowerCase().replace("__", ".").replace("_", "-"));

	@Override
	public MessageKey getMessageKey() {
		return key;
	}
}