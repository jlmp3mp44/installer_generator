package org.example.builder;

import entities.ConversionSettings;
import entities.InputFile;
import entities.OutputFile;
import java.util.List;

public interface InstallerBuilder {
  void addFile(InputFile file);
  void setConversionSettings(ConversionSettings settings);
  void setOutputFile(OutputFile outputFile);
  Installer build();
}
