package types;

public class Tile extends XMLTemplate {
    private int blendPriority = 0;
    private String randomTexture = "";
    private int minDamage = 0;
    private int maxDamage = 0;
    private double speed = 0.0;
    private double flowDX = 0.0;
    private double flowDY = 0.0;
    private boolean sink = false;
    private boolean noWalk = false;

    public double getFlowDX() {
        return flowDX;
    }

    public void setFlowDX(double flowDX) {
        this.flowDX = flowDX;
    }

    public double getFlowDY() {
        return flowDY;
    }

    public void setFlowDY(double flowDY) {
        this.flowDY = flowDY;
    }

    public int getBlendPriority() {
        return blendPriority;
    }

    public void setBlendPriority(int blendPriority) {
        this.blendPriority = blendPriority;
    }

    public String getRandomTexture() {
        return randomTexture;
    }

    public void setRandomTexture(String randomTexture) {
        this.randomTexture = randomTexture;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isSink() {
        return sink;
    }

    public void setSink(boolean sink) {
        this.sink = sink;
    }

    public boolean isNoWalk() {
        return noWalk;
    }

    public void setNoWalk(boolean noWalk) {
        this.noWalk = noWalk;
    }
}
