package sk.tomsik68.pw;
/** Weather rarity in %s
 * 
 * @author Tomsik68
 *
 */
public enum EWeatherRarity {
    /**15%
     * 
     */
	ExtremelyRare(15),
	/** 25%
	 * 
	 */
	Rare(25),
	/** 50%
	 * 
	 */
	Normal(50),
    /** 75%
     * 
     */
	Often(75),
	/**
	 * 90%
	 */
	VeryOften(90);

	private final int apart;

	private EWeatherRarity(int a) {
		this.apart = a;
	}

	public int getApart() {
		return this.apart;
	}
}