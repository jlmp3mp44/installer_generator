package org.example.builder;


import entities.ConversionSettings;
import entities.InputFile;
import entities.OutputFile;

public class InstallerBuilderImpl implements InstallerBuilder {
  private InputFile file;
  private ConversionSettings settings;
  private OutputFile outputFile;

  @Override
  public void addFile(InputFile file) {
    this.file = file;
  }

  @Override
  public void setConversionSettings(ConversionSettings settings) {
    this.settings = settings;
  }

  @Override
  public void setOutputFile(OutputFile outputFile) {
    this.outputFile = outputFile;
  }

  @Override
  public Installer build() {
    return new Installer(file, settings, outputFile);
  }
}

