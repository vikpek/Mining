/**
 * 
 */
package at.ac.uibk.inforet.twittermood;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TokenSerializable implements Serializable {

	/**
	 * 
	 */
	private static final String SER_PATH = "token.ser";
	private static final long serialVersionUID = 1L;
	private String token;
	private String tokenSecret;

	private static TokenSerializable instance = new TokenSerializable();

	private TokenSerializable() {

	}

	public static TokenSerializable getInstance() {
		return instance;
	}

	public void serialize() {
		try {
			final FileOutputStream fileOut = new FileOutputStream(SER_PATH);
			final ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(TokenSerializable.getInstance());
			out.close();
			fileOut.close();
		} catch (final IOException i) {
			i.printStackTrace();
		}
	}

	public void tryToDeserialize() {
		final File f = new File(SER_PATH);
		if (f.exists()) {
			System.out.println("Exisiting tokens has been found. Loading...");
			try {
				final FileInputStream fileIn = new FileInputStream(SER_PATH);
				final ObjectInputStream in = new ObjectInputStream(fileIn);
				TokenSerializable.setInstance((TokenSerializable) in
						.readObject());
				in.close();
				fileIn.close();

			} catch (final IOException i) {
				i.printStackTrace();
			} catch (final ClassNotFoundException c) {
				System.out.println("No token class found");
				c.printStackTrace();
			}
		}
	}

	public boolean tokenExists() {
		final File f = new File(SER_PATH);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static void setInstance(TokenSerializable instance) {
		TokenSerializable.instance = instance;
	}

	public String getTokenSecret() {
		if(tokenSecret == null){
			System.out.println("Empty scret token - create new tokens.");
			System.exit(1);
		}
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getToken() {
		if(token == null){
			System.out.println("Empty token - create new tokens.");
			System.exit(1);
		}
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
