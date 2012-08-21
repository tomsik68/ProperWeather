package sk.tomsik68.pw;

public enum EWeatherRarity {
	ExtremelyRare(15),

	Rare(25),

	Normal(50),

	Often(75),

	VeryOften(90);

	private final int apart;

	private EWeatherRarity(int a) {
		this.apart = a;
	}

	public int getApart() {
		return this.apart;
	}
}