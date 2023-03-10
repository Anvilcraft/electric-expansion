package electricexpansion.api;

public enum EnumWireMaterial {
  COPPER("Copper", 0.0125f, 3, 200),
  TIN("Tin", 0.01f, 2, 30),
  SILVER("Silver", 0.005f, 1, 300),
  ALUMINUM("Aluminum", 0.025f, 8, 15),
  SUPERCONDUCTOR("Superconductor", 0.0f, 5, Integer.MAX_VALUE),
  UNKNOWN("Unknown", 0.4f, 2, 60);

  public final String name;
  public final float resistance;
  public final int electrocutionDamage;
  public final int maxAmps;

  private EnumWireMaterial(final String name, final float resistance,
                           final int electrocutionDamage, final int maxAmps) {
    this.name = name;
    this.resistance = resistance;
    this.electrocutionDamage = electrocutionDamage;
    this.maxAmps = maxAmps;
  }
}
