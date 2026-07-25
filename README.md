# Elitech Bluetooth Data Logger Java SDK

A production-grade, highly-extensible, cross-platform Java SDK reverse-engineered from the Elitech desktop application. Provides a fluent high-level API to connect, read, configure, and generate CSV, TXT, Excel (XLSX), and PDF reports from any Elitech data logger.

---

## Features

- **Fluent High-Level API**: Easy-to-use API syntax for all typical logging procedures.
- **Cross-Platform Support**: Works on Windows, macOS, and Linux out-of-the-box.
- **Constructor-Based Dependency Injection**: Instantiates device clients directly with the chosen transport, facilitating clean decoupling and easy mocking.
- **Protocol Frame Assembly**: Accurate binary framing for supported operations: `GetParameter`, `SetParameter`, `GetRecord`, `Format`, and `Stop` command sequences.
- **Universal Exporters**: Beautiful, rich document exporters covering PDF, CSV, TXT, and Excel (XLSX) reports containing complete logger diagnostics, JFreeChart graphs, and record tables.

---

## Getting Started

### 1. Requirements
- **Java**: SE 17 or higher
- **Build Tool**: Apache Maven 3.8+

### 2. Dependency Inclusion
To include the SDK, build the project and import the dependencies:
- **OpenPDF**: For standard open-source PDF reporting.
- **Apache POI**: For XLSX spreadsheet reports.
- **jSerialComm**: Cross-platform pure-Java serial framework.
- **JFreeChart**: For PDF report charts rendering.

---

## Code Examples

### Fluent Connection & Diagnostic Acquisition
```java
import elitech.api.ElitechDevice;
import elitech.api.MockUsbTransport;
import elitech.model.Parameters;
import elitech.model.Record;
import elitech.report.CsvExporter;
import elitech.report.ExcelExporter;
import elitech.report.PdfExporter;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // 1. Initialize Elitech Device using Constructor-based Dependency Injection
        ElitechDevice device = new ElitechDevice(new MockUsbTransport());

        // 2. Execute connection, read parameters, and download records fluently
        device.connect()
              .readParameters()
              .downloadRecords();

        // 3. Inspect details
        Parameters params = device.getParameters();
        System.out.println("Serial: " + params.getSerialNum());
        System.out.println("State: " + params.getDeviceStateValue());

        List<Record> records = device.getRecords();
        System.out.println("Downloaded logs: " + records.size());

        // 4. Export to multi-format reports
        CsvExporter.exportToCsv("report.csv", params, records);
        PdfExporter.exportToPdf("report.pdf", params, records);
        ExcelExporter.exportToExcel("report.xlsx", params, records);

        // 5. Safe disconnection
        device.disconnect();
    }
}
```

---

## Project Structure

```
├── pom.xml                   # Maven Build Configuration
├── PROTOCOL.md               # Raw Byte-level Protocol Specification
├── README.md                 # Project Overview and Quickstart Guide
└── src
    ├── main/java/elitech/
    │   ├── api/              # Elitech API Client & Transport Layer (direct DI connection classes)
    │   ├── model/            # Logger Parameters, Record Models, and Device registry
    │   ├── protocol/         # Packet assemblers and parsing engine
    │   └── report/           # CSV, TXT, Excel (Apache POI), and PDF exporters with JFreeChart
    └── test/java/elitech/    # Comprehensive JUnit 5 Test Suite
```

---

## Running the Tests
To run all verification and integration test suites:
```bash
mvn test
```
