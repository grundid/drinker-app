package de.grundid.drinker.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.grundid.drinker.utils.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumePrice {

	private int price;
	private Integer volume;
	private transient Double pricePerVolume;

	public Double getPricePerVolume() {
		if (pricePerVolume == null) {
			pricePerVolume = Utils.getPricePerVolume(price, volume);
		}
		return pricePerVolume;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}
}
