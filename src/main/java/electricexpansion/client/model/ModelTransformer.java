package electricexpansion.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelTransformer extends ModelBase {
    ModelRenderer a;
    ModelRenderer b;
    ModelRenderer c;
    ModelRenderer d;
    ModelRenderer out2;
    ModelRenderer out1;
    ModelRenderer out3;
    ModelRenderer out4;
    ModelRenderer i;
    ModelRenderer j;
    ModelRenderer in1;
    ModelRenderer in2;
    ModelRenderer in3;
    ModelRenderer in4;

    public ModelTransformer() {
        super.textureWidth = 70;
        super.textureHeight = 45;
        (this.a = new ModelRenderer((ModelBase) this, 0, 0))
                .addBox(-8.0f, 0.0f, -8.0f, 16, 2, 16);
        this.a.setRotationPoint(0.0f, 22.0f, 0.0f);
        this.a.setTextureSize(70, 45);
        this.a.mirror = true;
        this.setRotation(this.a, 0.0f, 0.0f, 0.0f);
        (this.b = new ModelRenderer((ModelBase) this, 0, 19))
                .addBox(0.0f, 0.0f, -2.0f, 3, 11, 4);
        this.b.setRotationPoint(5.0f, 11.0f, 0.0f);
        this.b.setTextureSize(70, 45);
        this.b.mirror = true;
        this.setRotation(this.b, 0.0f, 0.0f, 0.0f);
        (this.c = new ModelRenderer((ModelBase) this, 0, 19))
                .addBox(0.0f, 0.0f, -2.0f, 3, 11, 4);
        this.c.setRotationPoint(-8.0f, 11.0f, 0.0f);
        this.c.setTextureSize(70, 45);
        this.c.mirror = true;
        this.setRotation(this.c, 0.0f, 0.0f, 0.0f);
        (this.d = new ModelRenderer((ModelBase) this, 15, 19))
                .addBox(0.0f, 0.0f, -2.0f, 16, 1, 4);
        this.d.setRotationPoint(-8.0f, 10.0f, 0.0f);
        this.d.setTextureSize(70, 45);
        this.d.mirror = true;
        this.setRotation(this.d, 0.0f, 0.0f, 0.0f);
        (this.out2 = new ModelRenderer((ModelBase) this, 0, 35))
                .addBox(0.0f, 0.0f, -3.0f, 5, 0, 6);
        this.out2.setRotationPoint(-9.0f, 16.0f, 0.0f);
        this.out2.setTextureSize(70, 45);
        this.out2.mirror = true;
        this.setRotation(this.out2, 0.0f, 0.0f, 0.0f);
        (this.out1 = new ModelRenderer((ModelBase) this, 0, 35))
                .addBox(0.0f, 0.0f, -3.0f, 5, 0, 6);
        this.out1.setRotationPoint(-9.0f, 15.0f, 0.0f);
        this.out1.setTextureSize(70, 45);
        this.out1.mirror = true;
        this.setRotation(this.out1, 0.0f, 0.0f, 0.0f);
        (this.out3 = new ModelRenderer((ModelBase) this, 0, 35))
                .addBox(0.0f, 0.0f, -3.0f, 5, 0, 6);
        this.out3.setRotationPoint(-9.0f, 17.0f, 0.0f);
        this.out3.setTextureSize(70, 45);
        this.out3.mirror = true;
        this.setRotation(this.out3, 0.0f, 0.0f, 0.0f);
        (this.out4 = new ModelRenderer((ModelBase) this, 0, 35))
                .addBox(0.0f, 0.0f, -3.0f, 5, 0, 6);
        this.out4.setRotationPoint(-9.0f, 18.0f, 0.0f);
        this.out4.setTextureSize(70, 45);
        this.out4.mirror = true;
        this.setRotation(this.out4, 0.0f, 0.0f, 0.0f);
        (this.i = new ModelRenderer((ModelBase) this, 34, 35))
                .addBox(0.0f, 0.0f, -1.0f, 2, 5, 2);
        this.i.setRotationPoint(-10.0f, 14.0f, 0.0f);
        this.i.setTextureSize(70, 45);
        this.i.mirror = true;
        this.setRotation(this.i, 0.0f, 0.0f, 0.0f);
        (this.j = new ModelRenderer((ModelBase) this, 24, 35))
                .addBox(0.0f, 0.0f, -1.0f, 2, 5, 2);
        this.j.setRotationPoint(8.0f, 14.0f, 0.0f);
        this.j.setTextureSize(70, 45);
        this.j.mirror = true;
        this.setRotation(this.j, 0.0f, 0.0f, 0.0f);
        (this.in1 = new ModelRenderer((ModelBase) this, 0, 35))
                .addBox(0.0f, 0.0f, -3.0f, 5, 0, 6);
        this.in1.setRotationPoint(4.0f, 15.0f, 0.0f);
        this.in1.setTextureSize(70, 45);
        this.in1.mirror = true;
        this.setRotation(this.in1, 0.0f, 0.0f, 0.0f);
        (this.in2 = new ModelRenderer((ModelBase) this, 0, 35))
                .addBox(0.0f, 0.0f, -3.0f, 5, 0, 6);
        this.in2.setRotationPoint(4.0f, 16.0f, 0.0f);
        this.in2.setTextureSize(70, 45);
        this.in2.mirror = true;
        this.setRotation(this.in2, 0.0f, 0.0f, 0.0f);
        (this.in3 = new ModelRenderer((ModelBase) this, 0, 35))
                .addBox(0.0f, 0.0f, -3.0f, 5, 0, 6);
        this.in3.setRotationPoint(4.0f, 17.0f, 0.0f);
        this.in3.setTextureSize(70, 45);
        this.in3.mirror = true;
        this.setRotation(this.in3, 0.0f, 0.0f, 0.0f);
        (this.in4 = new ModelRenderer((ModelBase) this, 0, 35))
                .addBox(0.0f, 0.0f, -3.0f, 5, 0, 6);
        this.in4.setRotationPoint(4.0f, 18.0f, 0.0f);
        this.in4.setTextureSize(70, 45);
        this.in4.mirror = true;
        this.setRotation(this.in4, 0.0f, 0.0f, 0.0f);
    }

    public void render(final Entity entity, final float f, final float f1,
            final float f2, final float f3, final float f4,
            final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.a.render(f5);
        this.b.render(f5);
        this.c.render(f5);
        this.d.render(f5);
        this.out2.render(f5);
        this.out1.render(f5);
        this.out3.render(f5);
        this.out4.render(f5);
        this.i.render(f5);
        this.j.render(f5);
        this.in1.render(f5);
        this.in2.render(f5);
        this.in3.render(f5);
        this.in4.render(f5);
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
