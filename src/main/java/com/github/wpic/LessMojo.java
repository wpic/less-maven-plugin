package com.github.wpic;

import com.inet.lib.less.Less;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

/**
 * Simple and fast less compiler maven mojo plugin.
 */
@Mojo( name = "less")
public class LessMojo extends AbstractMojo {

    @Parameter( property = "compiles", required = true )
    private List<Compile> compiles;

    public void execute() throws MojoExecutionException, MojoFailureException {
        for (Compile compile:this.compiles) {
            if (compile.from == null) {
                throw new MojoFailureException("Source file (from) is not set");
            }

            if (!compile.from.exists()) {
                throw new MojoFailureException("Source file (from) does not find: " + compile.from);
            }
            if (!compile.from.isFile()) {
                throw new MojoFailureException("Source file (from) is not directory: " + compile.from);
            }

            final String css;
            try {
                css = Less.compile(compile.from, Boolean.TRUE.equals(compile.compress));
            }
            catch (Exception ex) {
                getLog().error(ex);
                throw new MojoFailureException("Error to compile: " + compile.from);
            }

            if (compile.append != null && compile.to != null) {
                throw new MojoExecutionException(
                        "Just one of append (" + compile.append + ") and to (" + compile.to + ") can set!");
            }

            final File dest;
            final boolean append;
            if (compile.to != null) {
                dest = compile.to;
                append = false;
            }
            else if (compile.append != null) {
                dest = compile.append;
                append = true;
            }
            else {
                final String name = compile.from.getName();
                if (name.toLowerCase().endsWith(".less")) {
                    dest = new File(compile.from.getPath().substring(0, compile.from.getPath().length() - 5) + ".css");
                    append = false;
                }
                else {
                    dest = new File(compile.from.getPath() + ".css");
                    append = false;
                }
            }

            try {
                if (dest.isDirectory()) {
                    throw new MojoFailureException("Destination file (to) is exist and it's a directory: " + compile.from);
                }

                final File dir = dest.getParentFile();
                if (!dir.exists() && !dir.mkdirs()) {
                    throw new MojoFailureException("Can not create output directory: " + dir);
                }

                FileUtils.writeStringToFile(dest, css, append);
            }
            catch (Exception ex) {
                getLog().error(ex);
                throw new MojoFailureException("Error to write css: " + dest);
            }

        }
    }

}