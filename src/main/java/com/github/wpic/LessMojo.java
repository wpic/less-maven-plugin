package com.github.wpic;

import com.inet.lib.less.Less;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.List;

/**
 * Simple and fast less compiler maven mojo plugin.
 */
@Mojo( name = "less")
public class LessMojo extends AbstractMojo {

    @Parameter( defaultValue = "${project.basedir}" )
    private File baseDir;

    @Parameter( property = "compiles", required = true )
    private List<Compile> compiles;

    public void execute() throws MojoExecutionException, MojoFailureException {
        for (Compile compile:this.compiles) {
            if (compile.from == null) {
                throw new MojoFailureException("Source file (from) is not set");
            }

            final DirectoryScanner scanner = new DirectoryScanner();
            scanner.setIncludes(new String[]{compile.from});
            scanner.setBasedir(baseDir);
            scanner.setCaseSensitive(false);
            scanner.scan();

            final String[] files = scanner.getIncludedFiles();
            if (files.length == 0) {
                throw new MojoFailureException("No source file found: " + compile.from);
            }

            if (compile.append != null && compile.to != null) {
                throw new MojoExecutionException(
                        "Just one of append (" + compile.append + ") and to (" + compile.to + ") can set!");
            }

            // Without to and without append
            else if (compile.to == null && compile.append == null) {
                for (String f : files) {
                    try {
                        final File from = new File(baseDir, f);
                        final String css = Less.compile(from, Boolean.TRUE.equals(compile.compress));
                        final String name = from.getName();

                        final File dest;
                        if (name.toLowerCase().endsWith(".less")) {
                            dest = new File(from.getPath().substring(0, from.getPath().length() - 5) + ".css");
                        } else {
                            dest = new File(from.getPath() + ".css");
                        }
                        FileUtils.writeStringToFile(dest, css, false);
                    } catch (Exception ex) {
                        getLog().error(ex);
                        throw new MojoFailureException("Error to compile: " + compile.from);
                    }
                }
            }

            // With to and without append
            else if (compile.to != null) {
                final StringBuilder css = new StringBuilder();

                for (String f : files) {
                    try {
                        final File from = new File(baseDir, f);

                        css.append(Less.compile(from, Boolean.TRUE.equals(compile.compress)));
                        css.append('\n');
                    } catch (Exception ex) {
                        getLog().error(ex);
                        throw new MojoFailureException("Error to compile: " + compile.from);
                    }
                }

                try {
                    FileUtils.writeStringToFile(compile.to, css.toString(), false);
                } catch (Exception ex) {
                    getLog().error(ex);
                    throw new MojoFailureException("Error to save: " + compile.from);
                }
            }

            // Without to and with append
            else if (compile.append != null) {
                final StringBuilder css = new StringBuilder();

                for (String f : files) {
                    try {
                        final File from = new File(baseDir, f);

                        css.append(Less.compile(from, Boolean.TRUE.equals(compile.compress)));
                        css.append('\n');
                    } catch (Exception ex) {
                        getLog().error(ex);
                        throw new MojoFailureException("Error to compile: " + compile.append);
                    }
                }

                try {
                    FileUtils.writeStringToFile(compile.append, css.toString(), true);
                } catch (Exception ex) {
                    getLog().error(ex);
                    throw new MojoFailureException("Error to save: " + compile.append);
                }
            }

        }
    }

}