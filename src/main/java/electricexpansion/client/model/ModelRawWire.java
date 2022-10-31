package electricexpansion.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelRawWire extends ModelBase {
    ModelRenderer Middle;
    ModelRenderer Right;
    ModelRenderer Back;
    ModelRenderer Left;
    ModelRenderer Front;
    ModelRenderer Bottom;
    ModelRenderer Top;

    public ModelRawWire() {
        super.textureWidth = 64;
        super.textureHeight = 32;
        (this.Middle = new ModelRenderer((ModelBase) this, 0, 0))
                .addBox(0.0f, 0.0f, 0.0f, 2, 2, 2);
        this.Middle.setRotationPoint(-1.0f, 15.0f, -1.0f);
        this.Middle.setTextureSize(64, 32);
        this.Middle.mirror = true;
        this.setRotation(this.Middle, 0.0f, 0.0f, 0.0f);
        (this.Right = new ModelRenderer((ModelBase) this, 22, 0))
                .addBox(0.0f, 0.0f, 0.0f, 7, 2, 2);
        this.Right.setRotationPoint(1.0f, 15.0f, -1.0f);
        this.Right.setTextureSize(64, 32);
        this.Right.mirror = true;
        this.setRotation(this.Right, 0.0f, 0.0f, 0.0f);
        (this.Back = new ModelRenderer((ModelBase) this, 0, 10))
                .addBox(0.0f, 0.0f, 0.0f, 2, 2, 7);
        this.Back.setRotationPoint(-1.0f, 15.0f, 1.0f);
        this.Back.setTextureSize(64, 32);
        this.Back.mirror = true;
        this.setRotation(this.Back, 0.0f, 0.0f, 0.0f);
        (this.Left = new ModelRenderer((ModelBase) this, 44, 0))
                .addBox(0.0f, 0.0f, 0.0f, 7, 2, 2);
        this.Left.setRotationPoint(-8.0f, 15.0f, -1.0f);
        this.Left.setTextureSize(64, 32);
        this.Left.mirror = true;
        this.setRotation(this.Left, 0.0f, 0.0f, 0.0f);
        (this.Front = new ModelRenderer((ModelBase) this, 0, 22))
                .addBox(0.0f, 0.0f, 0.0f, 2, 2, 7);
        this.Front.setRotationPoint(-1.0f, 15.0f, -8.0f);
        this.Front.setTextureSize(64, 32);
        this.Front.mirror = true;
        this.setRotation(this.Front, 0.0f, 0.0f, 0.0f);
        (this.Bottom = new ModelRenderer((ModelBase) this, 22, 10))
                .addBox(0.0f, 0.0f, 0.0f, 2, 7, 2);
        this.Bottom.setRotationPoint(-1.0f, 17.0f, -1.0f);
        this.Bottom.setTextureSize(64, 32);
        this.Bottom.mirror = true;
        this.setRotation(this.Bottom, 0.0f, 0.0f, 0.0f);
        (this.Top = new ModelRenderer((ModelBase) this, 22, 22))
                .addBox(0.0f, 0.0f, 0.0f, 2, 7, 2);
        this.Top.setRotationPoint(-1.0f, 8.0f, -1.0f);
        this.Top.setTextureSize(64, 32);
        this.Top.mirror = true;
        this.setRotation(this.Top, 0.0f, 0.0f, 0.0f);
    }

    public void renderMiddle() {
        this.Middle.render(0.0625f);
    }

    public void renderBottom() {
        this.Bottom.render(0.0625f);
    }

    public void renderTop() {
        this.Top.render(0.0625f);
    }

    public void renderLeft() {
        this.Left.render(0.0625f);
    }

    public void renderRight() {
        this.Right.render(0.0625f);
    }

    public void renderBack() {
        this.Back.render(0.0625f);
    }

    public void renderFront() {
        this.Front.render(0.0625f);
    }

    private void setRotation(final ModelRenderer model, final float x,
            final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
