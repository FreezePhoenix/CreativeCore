package team.creative.creativecore.client.render.model;

import com.mojang.blaze3d.platform.MemoryTracker;
import com.mojang.blaze3d.vertex.BufferBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.creative.creativecore.mixin.BufferBuilderAccessor;

import java.nio.ByteBuffer;

public class BufferBuilderUtils {

	private static final Logger LOGGER = LogManager.getLogger();

	public static void ensureCapacity(BufferBuilder builder, int size) {
		((BufferBuilderAccessor) builder).invokeEnsureCapacity(size);
	}

	public static void ensureTotalSize(BufferBuilder builder, int size) {
		int toAdd = size - getBufferSizeByte(builder);
		if (size > 0) {
			growBufferSmall(builder, toAdd);
		}
	}

	public static ByteBuffer getBuffer(BufferBuilder builder) {
		return ((BufferBuilderAccessor) builder).getBuffer();
	}

	public static int getVertexCount(BufferBuilder builder) {
		return ((BufferBuilderAccessor) builder).getVertices();
	}

	public static void growBufferSmall(BufferBuilder builder, int size) {
		ByteBuffer buffer = ((BufferBuilderAccessor) builder).getBuffer();
		if (((BufferBuilderAccessor) builder).getNextElementByte() + size > buffer.capacity()) {
			LOGGER.debug(
					"Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.",
					buffer.capacity(),
					buffer.capacity() + size
			);
			ByteBuffer bytebuffer = MemoryTracker.resize(buffer, buffer.capacity() + size);
			bytebuffer.rewind();
			((BufferBuilderAccessor) builder).setBuffer(bytebuffer);
		}
	}

	public static int getBufferSizeInt(BufferBuilder builder) {
		return ((BufferBuilderAccessor) builder).getFormat().getIntegerSize() * getVertexCount(builder);
	}

	public static int getBufferSizeByte(BufferBuilder builder) {
		return ((BufferBuilderAccessor) builder).getFormat().getVertexSize() * getVertexCount(builder);
	}
    
    /*public static int get(BufferBuilder builder, int index) {
        try {
            return ((IntBuffer) rawIntBufferField.get(builder)).get(index);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static void addVertexDataSmall(BufferBuilder builder, int[] vertexData) {
        growBufferSmall(builder, vertexData.length * 4 + builder.getVertexFormat().getVertexSize());
        try {
            IntBuffer intBuffer = (IntBuffer) rawIntBufferField.get(builder);
            intBuffer.position(getBufferSizeInt(builder));
            intBuffer.put(vertexData);
            vertexCountField.setInt(builder, vertexCountField.getInt(builder) + vertexData.length / builder.getVertexFormat().getIntegerSize());
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public static void addBuffer(BufferBuilder buffer, BufferBuilder toAdd) {
        int size = toAdd.getVertexFormat().getIntegerSize() * toAdd.getVertexCount();
        try {
            IntBuffer rawIntBuffer = (IntBuffer) rawIntBufferField.get(toAdd);
            rawIntBuffer.rewind();
            rawIntBuffer.limit(size);
            
            //growBuffer(buffer, size + buffer.getVertexFormat().getNextOffset());
            IntBuffer chunkIntBuffer = (IntBuffer) rawIntBufferField.get(buffer);
            chunkIntBuffer.position(getBufferSizeInt(buffer));
            chunkIntBuffer.put(rawIntBuffer);
            
            vertexCountField.setInt(buffer, buffer.getVertexCount() + toAdd.getVertexCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

	public static void addBuffer(BufferBuilder buffer, ByteBuffer toAdd, int length, int count) {
		try {
			toAdd.rewind();
			toAdd.limit(length);

			//growBuffer(buffer, size + buffer.getVertexFormat().getNextOffset());
			ByteBuffer chunkByteBuffer = ((BufferBuilderAccessor) buffer).getBuffer();
			int size = getBufferSizeByte(buffer);
			chunkByteBuffer.limit(size + length);
			chunkByteBuffer.position(size);

			chunkByteBuffer.put(toAdd);

			chunkByteBuffer.rewind();
			((BufferBuilderAccessor) buffer).setVertices(((BufferBuilderAccessor) buffer).getVertices() + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
