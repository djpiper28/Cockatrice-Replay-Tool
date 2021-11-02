class Player {
    @Override
    public String toString() {
        return "\nPlayer{" +
                "deckHash='" + deckHash + '\'' +
                ", name='" + name + '\'' +
                ", table position=" + id +
                ", handsDrawn=" + handsDrawn +
                "}";
    }

    private String deckHash;
    private final String name;
    private int id, handsDrawn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHandsDrawn() {
        return handsDrawn;
    }

    public void setHandsDrawn(int handsDrawn) {
        this.handsDrawn = handsDrawn;
    }

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getDeckHash() {
        return this.deckHash;
    }

    public void setDeckHash(String deckHash) {
        this.deckHash = deckHash;
    }

    public boolean isSpectator(int max) {
        return (this.id < 1 && this.id > max) || this.deckHash == null;
    }

    public String getCSV() {
        return String.format("%d,%s,%s,%d", id, name, deckHash, handsDrawn);
    }
}
