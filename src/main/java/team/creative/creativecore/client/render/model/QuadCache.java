package team.creative.creativecore.client.render.model;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import team.creative.creativecore.mixin.VertexFormatAccessor;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public class QuadCache {

	public final ArrayList<int[]> quadDatas = new ArrayList<>();
	public VertexFormat format;
	private int v = 0;
	private int[] quadData;

	public QuadCache(VertexFormat format) {
		this.format = format;
	}

	// Forge LightUtil::pack
	public static void pack(float[] from, int[] to, VertexFormat formatTo, int v, int e) {
		VertexFormatElement element = formatTo.getElements().get(e);
		int vertexStart = v * formatTo.getVertexSize() + ((VertexFormatAccessor) formatTo).getOffsets().getInt(e);
		int count = element.getCount();
		VertexFormatElement.Type type = element.getType();
		int size = type.getSize();
		int mask = (256 << (8 * (size - 1))) - 1;
		for (int i = 0; i < 4; i++) {
			if (i < count) {
				int pos = vertexStart + size * i;
				int index = pos >> 2;
				int offset = pos & 3;
				int bits = 0;
				float f = i < from.length
				          ? from[i]
				          : 0;
				if (type == VertexFormatElement.Type.FLOAT) {
					bits = Float.floatToRawIntBits(f);
				} else if (
						type == VertexFormatElement.Type.UBYTE ||
								type == VertexFormatElement.Type.USHORT ||
								type == VertexFormatElement.Type.UINT
				) {
					bits = Math.round(f * mask);
				} else {
					bits = Math.round(f * (mask >> 1));
				}
				to[index] &= ~(mask << (offset * 8));
				to[index] |= (((bits & mask) << (offset * 8)));
				// TODO handle overflow into to[index + 1]
			}
		}
	}

	public void addCache(int index, float... cache) {
		if (quadData == null)
			quadData = new int[format.getVertexSize()];
		pack(cache, quadData, format, v, index);
		if (index == format.getElements().size() - 1) {
			v++;
			if (v == 4) {
				quadDatas.add(quadData);
				quadData = null;
				v = 0;
			}
		}
	}

}
