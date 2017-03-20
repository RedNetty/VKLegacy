package me.kayaba.guilds.impl.util.exceptionparser;

import me.kayaba.guilds.api.util.exceptionparser.*;

import java.util.*;

public class ErrorSignatureImpl implements ErrorSignature {
    private final Collection<Block> blocks = new LinkedHashSet<>();


    public ErrorSignatureImpl(IError error) {
        addBlock(new BlockImpl(error.getException()));

        for (Throwable cause : error.getCauses()) {
            addBlock(new BlockImpl(cause));
        }
    }

    @Override
    public Collection<Block> getBlocks() {
        return blocks;
    }

    @Override
    public void addBlock(Block... block) {
        Collections.addAll(blocks, block);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        Iterator<Block> iterator = getBlocks().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            builder.append(block.getName())
                    .append("|")
                    .append(block.getMessage())
                    .append("|")
                    .append(block.getStackTraceElement());

            if (iterator.hasNext()) {
                builder.append("|");
            }
        }

        return builder.toString();
    }
}
