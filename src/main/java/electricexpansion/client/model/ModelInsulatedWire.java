package electricexpansion.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelInsulatedWire extends ModelBase {
    ModelRenderer Middle;
    ModelRenderer Right;
    ModelRenderer Left;
    ModelRenderer Back;
    ModelRenderer Front;
    ModelRenderer Top;
    ModelRenderer Bottom;

    public ModelInsulatedWire() {
        super.textureWidth = 64;
        super.textureHeight = 32;
        (this.Middle = new ModelRenderer((ModelBase) this, 0, 0))
                .addBox(0.0f, 0.0f, 0.0f, 4, 4, 4);
        this.Middle.setRotationPoint(-2.0f, 14.0f, -2.0f);
        this.Middle.setTextureSize(super.textureWidth, super.textureHeight);
        this.Middle.mirror = true;
        this.setRotation(this.Middle, 0.0f, 0.0f, 0.0f);
        (this.Right = new ModelRenderer((ModelBase) this, 22, 0))
                .addBox(0.0f, 0.0f, 0.0f, 6, 4, 4);
        this.Right.setRotationPoint(2.0f, 14.0f, -2.0f);
        this.Right.setTextureSize(super.textureWidth, super.textureHeight);
        this.Right.mirror = true;
        this.setRotation(this.Right, 0.0f, 0.0f, 0.0f);
        (this.Left = new ModelRenderer((ModelBase) this, 44, 0))
                .addBox(0.0f, 0.0f, 0.0f, 6, 4, 4);
        this.Left.setRotationPoint(-8.0f, 14.0f, -2.0f);
        this.Left.setTextureSize(super.textureWidth, super.textureHeight);
        this.Left.mirror = true;
        this.setRotation(this.Left, 0.0f, 0.0f, 0.0f);
        (this.Back = new ModelRenderer((ModelBase) this, 0, 10))
                .addBox(0.0f, 0.0f, 0.0f, 4, 4, 6);
        this.Back.setRotationPoint(-2.0f, 14.0f, 2.0f);
        this.Back.setTextureSize(super.textureWidth, super.textureHeight);
        this.Back.mirror = true;
        this.setRotation(this.Back, 0.0f, 0.0f, 0.0f);
        (this.Front = new ModelRenderer((ModelBase) this, 0, 22))
                .addBox(0.0f, 0.0f, 0.0f, 4, 4, 6);
        this.Front.setRotationPoint(-2.0f, 14.0f, -8.0f);
        this.Front.setTextureSize(super.textureWidth, super.textureHeight);
        this.Front.mirror = true;
        this.setRotation(this.Front, 0.0f, 0.0f, 0.0f);
        (this.Top = new ModelRenderer((ModelBase) this, 22, 22))
                .addBox(0.0f, 0.0f, 0.0f, 4, 6, 4);
        this.Top.setRotationPoint(-2.0f, 8.0f, -2.0f);
        this.Top.setTextureSize(super.textureWidth, super.textureHeight);
        this.Top.mirror = true;
        this.setRotation(this.Top, 0.0f, 0.0f, 0.0f);
        (this.Bottom = new ModelRenderer((ModelBase) this, 22, 10))
                .addBox(0.0f, 0.0f, 0.0f, 4, 6, 4);
        this.Bottom.setRotationPoint(-2.0f, 18.0f, -2.0f);
        this.Bottom.setTextureSize(super.textureWidth, super.textureHeight);
        this.Bottom.mirror = true;
        this.setRotation(this.Bottom, 0.0f, 0.0f, 0.0f);
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

    public void setRotationAngles(final float f, final float f1, final float f2,
            final float f3, final float f4, final float f5,
            final Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
