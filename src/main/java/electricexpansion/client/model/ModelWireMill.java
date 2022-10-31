package electricexpansion.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelWireMill extends ModelBase {
    ModelRenderer base;
    ModelRenderer plug;
    ModelRenderer part1;
    ModelRenderer part2;
    ModelRenderer part3;
    ModelRenderer part4;
    ModelRenderer support;
    ModelRenderer support2;
    ModelRenderer container;
    ModelRenderer support3;
    ModelRenderer support4;
    ModelRenderer gear1rot;
    ModelRenderer gear2rot;
    ModelRenderer output;

    public ModelWireMill() {
        super.textureWidth = 128;
        super.textureHeight = 128;
        (this.base = new ModelRenderer((ModelBase) this, 0, 0))
                .addBox(0.0f, 0.0f, 0.0f, 16, 1, 16);
        this.base.setRotationPoint(-8.0f, 23.0f, -8.0f);
        this.base.setTextureSize(128, 128);
        this.base.mirror = true;
        this.setRotation(this.base, 0.0f, 0.0f, 0.0f);
        (this.plug = new ModelRenderer((ModelBase) this, 0, 19))
                .addBox(0.0f, 0.0f, 0.0f, 5, 12, 12);
        this.plug.setRotationPoint(-7.0f, 11.0f, -4.0f);
        this.plug.setTextureSize(128, 128);
        this.plug.mirror = true;
        this.setRotation(this.plug, 0.0f, 0.0f, 0.0f);
        (this.part1 = new ModelRenderer((ModelBase) this, 0, 20))
                .addBox(0.0f, -1.0f, 0.0f, 1, 2, 1);
        this.part1.setRotationPoint(-8.0f, 14.0f, 0.0f);
        this.part1.setTextureSize(128, 128);
        this.part1.mirror = true;
        this.setRotation(this.part1, 0.0f, 0.0f, 0.0f);
        (this.part2 = new ModelRenderer((ModelBase) this, 0, 20))
                .addBox(0.0f, -1.0f, -3.0f, 1, 2, 1);
        this.part2.setRotationPoint(-8.0f, 14.0f, 0.0f);
        this.part2.setTextureSize(128, 128);
        this.part2.mirror = true;
        this.setRotation(this.part2, 0.0f, 0.0f, 0.0f);
        (this.part3 = new ModelRenderer((ModelBase) this, 0, 20))
                .addBox(0.0f, -2.0f, -1.0f, 1, 1, 2);
        this.part3.setRotationPoint(-8.0f, 14.0f, -1.0f);
        this.part3.setTextureSize(128, 128);
        this.part3.mirror = true;
        this.setRotation(this.part3, 0.0f, 0.0f, 0.0f);
        (this.part4 = new ModelRenderer((ModelBase) this, 0, 20))
                .addBox(0.0f, 1.0f, -1.0f, 1, 1, 2);
        this.part4.setRotationPoint(-8.0f, 14.0f, -1.0f);
        this.part4.setTextureSize(128, 128);
        this.part4.mirror = true;
        this.setRotation(this.part4, 0.0f, 0.0f, 0.0f);
        (this.support = new ModelRenderer((ModelBase) this, 0, 46))
                .addBox(0.0f, 0.0f, 0.0f, 5, 5, 4);
        this.support.setRotationPoint(-7.0f, 18.0f, -8.0f);
        this.support.setTextureSize(128, 128);
        this.support.mirror = true;
        this.setRotation(this.support, 0.0f, 0.0f, 0.0f);
        (this.support2 = new ModelRenderer((ModelBase) this, 19, 46))
                .addBox(0.0f, 0.0f, 0.0f, 5, 8, 4);
        this.support2.setRotationPoint(-7.0f, 11.0f, -4.0f);
        this.support2.setTextureSize(128, 128);
        this.support2.mirror = true;
        this.setRotation(this.support2, -0.5235988f, 0.0f, 0.0f);
        (this.container = new ModelRenderer((ModelBase) this, 48, 36))
                .addBox(0.0f, 0.0f, 0.0f, 10, 15, 5);
        this.container.setRotationPoint(-2.0f, 8.0f, 3.0f);
        this.container.setTextureSize(128, 128);
        this.container.mirror = true;
        this.setRotation(this.container, 0.0f, 0.0f, 0.0f);
        (this.support3 = new ModelRenderer((ModelBase) this, 80, 20))
                .addBox(0.0f, 0.0f, 0.0f, 2, 12, 4);
        this.support3.setRotationPoint(6.0f, 11.0f, -1.0f);
        this.support3.setTextureSize(128, 128);
        this.support3.mirror = true;
        this.setRotation(this.support3, 0.0f, 0.0f, 0.0f);
        (this.support4 = new ModelRenderer((ModelBase) this, 80, 20))
                .addBox(0.0f, 0.0f, 0.0f, 2, 12, 4);
        this.support4.setRotationPoint(-2.0f, 11.0f, -1.0f);
        this.support4.setTextureSize(128, 128);
        this.support4.mirror = true;
        this.setRotation(this.support4, 0.0f, 0.0f, 0.0f);
        (this.gear1rot = new ModelRenderer((ModelBase) this, 67, 13))
                .addBox(0.0f, -1.0f, -1.0f, 6, 2, 2);
        this.gear1rot.setRotationPoint(0.0f, 14.0f, 1.0f);
        this.gear1rot.setTextureSize(128, 128);
        this.gear1rot.mirror = true;
        this.setRotation(this.gear1rot, 0.7853982f, 0.0f, 0.0f);
        (this.gear2rot = new ModelRenderer((ModelBase) this, 67, 13))
                .addBox(0.0f, -1.0f, -1.0f, 6, 2, 2);
        this.gear2rot.setRotationPoint(0.0f, 17.0f, 1.0f);
        this.gear2rot.setTextureSize(128, 128);
        this.gear2rot.mirror = true;
        this.setRotation(this.gear2rot, 0.7853982f, 0.0f, 0.0f);
        (this.output = new ModelRenderer((ModelBase) this, 36, 20))
                .addBox(0.0f, 0.0f, 0.0f, 10, 4, 11);
        this.output.setRotationPoint(-2.0f, 19.0f, -8.0f);
        this.output.setTextureSize(128, 128);
        this.output.mirror = true;
        this.setRotation(this.output, 0.0f, 0.0f, 0.0f);
    }

    public void render(final Entity entity, final float f, final float f1,
            final float f2, final float f3, final float f4,
            final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.base.render(f5);
        this.plug.render(f5);
        this.part1.render(f5);
        this.part2.render(f5);
        this.part3.render(f5);
        this.part4.render(f5);
        this.support.render(f5);
        this.support2.render(f5);
        this.container.render(f5);
        this.support3.render(f5);
        this.support4.render(f5);
        this.gear1rot.render(f5);
        this.gear2rot.render(f5);
        this.output.render(f5);
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
