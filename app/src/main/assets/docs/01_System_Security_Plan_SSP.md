# SYSTEM SECURITY PLAN (SSP)

## Vivo T2X 5G Mobile Information System

---

| **Document Information** | |
|---|---|
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **System Identifier** | VTMIS-2026-001 |
| **System Owner** | AAKASH |
| **Organization** | AAKASH |
| **FedRAMP Impact Level** | Low / Moderate / High (Comprehensive) |
| **Document Version** | 1.0 |
| **Date** | March 9, 2026 |
| **Classification** | For Official Use Only (FOUO) |
| **Prepared By** | AAKASH, System Owner &amp; Information System Security Officer (ISSO) |

---

## TABLE OF CONTENTS

1. [Information System Overview](#1-information-system-overview)
2. [Information System Categorization](#2-information-system-categorization)
3. [Information System Owner](#3-information-system-owner)
4. [Authorizing Official](#4-authorizing-official)
5. [System Operational Status](#5-system-operational-status)
6. [Information System Type](#6-information-system-type)
7. [General System Description](#7-general-system-description)
8. [System Environment](#8-system-environment)
9. [System Interconnections](#9-system-interconnections)
10. [Laws, Regulations, Standards](#10-laws-regulations-and-standards)
11. [Minimum Security Controls](#11-minimum-security-controls---nist-sp-800-53-rev-5)
12. [Appendices](#12-appendices)

---

## 1. INFORMATION SYSTEM OVERVIEW

### 1.1 System Name and Identifier

- **System Name:** Vivo T2X 5G Mobile Information System (VTMIS)
- **Unique Identifier:** VTMIS-2026-001
- **System Abbreviation:** VTMIS

### 1.2 System Description

The Vivo T2X 5G Mobile Information System (VTMIS) is a mobile computing platform designed to receive, process, store, and transmit Federal information. The system is built on the Vivo T2X 5G (Model V2312) smartphone running Android 15 with FuntouchOS 15 (build PD2230KF_EX_A_15.2.13.1.W30), providing a portable endpoint for accessing federal data and executing federal applications.

The system provides the following capabilities:

- **Data Reception:** Downloading Federal information via cellular (Jio 5G/LTE) and Wi-Fi networks
- **Data Processing:** Executing federal applications on the MediaTek Dimensity 6020 octa-core processor
- **Data Storage:** Storing Federal information on the 128GB encrypted internal storage (file-based encryption)
- **Data Transmission:** Transmitting Federal data via encrypted channels over mobile networks
- **Peripheral Connectivity:** Tethering to a Windows 11 workstation (laptop) via USB cable and Wi-Fi hotspot for extended processing capabilities

### 1.3 System Function and Purpose

VTMIS serves as a mobile Federal information processing endpoint, enabling the system owner to:

1. Download Federal information from authorized government sources
2. Store Federal data on an encrypted, access-controlled mobile device
3. Process Federal data using installed applications
4. Transmit processed Federal data to authorized recipients
5. Provide network connectivity to a tethered workstation via Wi-Fi hotspot (IP: 172.18.50.78/24)

### 1.4 Information System Components

| Component | Details |
|---|---|
| **Device** | Vivo T2X 5G (Model V2312, Device V2225) |
| **Manufacturer** | vivo Mobile Communication Co., Ltd. |
| **Serial Number** | 10BDC81HTA000Z4 |
| **Processor** | MediaTek Dimensity 6020 (MT6833), Octa-core, 2.2 GHz, ARMv8.2-A (aarch64) |
| **CPU Features** | AES, PMULL, SHA1, SHA2, CRC32, Atomics, ASIMD |
| **RAM** | 6 GB Physical + 6 GB Dynamic Extension (Total ~5.5 GB usable, MemTotal: 5,755,288 kB) |
| **Internal Storage** | 128 GB (106 GB user-accessible, 97% utilized as of scan date) |
| **Display** | 6.6" IPS LCD, 1080 x 2408 pixels, 440 PPI |
| **Battery** | 5000 mAh Li-Polymer |
| **Operating System** | Android 15 (API Level 35) |
| **Custom OS** | FuntouchOS 15 (Build: PD2230KF_EX_A_15.2.13.1.W30) |
| **Kernel** | Linux 4.19.236+ SMP PREEMPT (compiled Sep 26, 2025) |
| **Security Patch Level** | May 1, 2025 |
| **Baseband** | MOLY.NR15.R3.TC19.PR4.SP.V1.P191 |
| **Build Fingerprint** | vivo/V2225C/V2225:15/AP3A.240905.015.A2_MOD1/compiler250926152015:user/release-keys |
| **Build Type** | user (production release) |

---

## 2. INFORMATION SYSTEM CATEGORIZATION

### 2.1 FIPS 199 Security Categorization

Per FIPS Publication 199 and NIST SP 800-60, the information system is categorized as follows:

#### Low Impact Categorization

| Security Objective | Impact Level | Justification |
|---|---|---|
| **Confidentiality** | Low | Limited adverse effect on operations |
| **Integrity** | Low | Limited adverse effect on operations |
| **Availability** | Low | Limited adverse effect on operations |

**Overall System Categorization (Low):** SC = {(Confidentiality, Low), (Integrity, Low), (Availability, Low)}

#### Moderate Impact Categorization

| Security Objective | Impact Level | Justification |
|---|---|---|
| **Confidentiality** | Moderate | Serious adverse effect on operations, assets, or individuals |
| **Integrity** | Moderate | Serious adverse effect on operations, assets, or individuals |
| **Availability** | Moderate | Serious adverse effect on operations, assets, or individuals |

**Overall System Categorization (Moderate):** SC = {(Confidentiality, Moderate), (Integrity, Moderate), (Availability, Moderate)}

#### High Impact Categorization

| Security Objective | Impact Level | Justification |
|---|---|---|
| **Confidentiality** | High | Severe or catastrophic adverse effect on operations, assets, or individuals |
| **Integrity** | High | Severe or catastrophic adverse effect on operations, assets, or individuals |
| **Availability** | High | Severe or catastrophic adverse effect on operations, assets, or individuals |

**Overall System Categorization (High):** SC = {(Confidentiality, High), (Integrity, High), (Availability, High)}

### 2.2 Information Types Processed

| Information Type | NIST SP 800-60 Category | Confidentiality | Integrity | Availability |
|---|---|---|---|---|
| General Government Records | C.2.8.1 | Moderate | Moderate | Moderate |
| Controlled Unclassified Information (CUI) | C.3.5.1 | Moderate | Moderate | Low |
| Personally Identifiable Information (PII) | C.3.5.2 | High | High | Moderate |
| Financial Records | C.3.2.1 | High | High | Moderate |
| Law Enforcement Sensitive | C.3.5.3 | High | High | High |

---

## 3. INFORMATION SYSTEM OWNER

| Field | Value |
|---|---|
| **Name** | AAKASH |
| **Title** | System Owner / Information System Security Officer (ISSO) |
| **Organization** | AAKASH |
| **Address** | [Address on File] |
| **Phone** | [Contact Number on File] |
| **Email** | [Email on File] |

---

## 4. AUTHORIZING OFFICIAL

| Field | Value |
|---|---|
| **Name** | [Designated Authorizing Official] |
| **Title** | Authorizing Official (AO) |
| **Organization** | [Federal Agency Name] |
| **Authorization Date** | [Pending] |
| **ATO Expiration** | [Pending — Typically 3 years from authorization] |

---

## 5. SYSTEM OPERATIONAL STATUS

| Status | Description |
|---|---|
| **Operational** | ✅ The system is currently operational |
| **Under Development** | ☐ |
| **Major Modification** | ☐ |
| **Other** | ☐ |

**Deployment Date:** February 2024 (estimated based on system build dates)
**Last Security Assessment:** March 9, 2026 (Initial Assessment)

---

## 6. INFORMATION SYSTEM TYPE

- ☐ Major Application
- ✅ General Support System (GSS)
- ✅ Mobile Device / Mobile Information System
- ☐ Cloud Service Offering (CSO)
- ☐ Standalone System

---

## 7. GENERAL SYSTEM DESCRIPTION

### 7.1 System Architecture

The VTMIS architecture consists of:

```
┌─────────────────────────────────────────────────────────────────────┐
│                    AUTHORIZATION BOUNDARY                           │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │            VIVO T2X 5G (Primary System)                     │   │
│  │                                                             │   │
│  │  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐   │   │
│  │  │ Application  │  │  FuntouchOS  │  │  Linux Kernel   │   │   │
│  │  │    Layer     │  │   15 (UI)    │  │  4.19.236+      │   │   │
│  │  │              │  │              │  │                  │   │   │
│  │  │ - Federal    │  │ - Android 15 │  │ - SELinux       │   │   │
│  │  │   Apps       │  │ - API 35     │  │   (Enforcing)   │   │   │
│  │  │ - User Apps  │  │ - File-Based │  │ - dm-verity     │   │   │
│  │  │   (38 pkgs)  │  │   Encryption │  │ - Verified Boot │   │   │
│  │  └──────────────┘  └──────────────┘  └────────────────┘   │   │
│  │                                                             │   │
│  │  ┌──────────────────────────────────────────────────────┐  │   │
│  │  │              HARDWARE LAYER                           │  │   │
│  │  │  CPU: MT6833 Dimensity 6020 (8-core ARMv8.2)        │  │   │
│  │  │  RAM: 5,755,288 kB (6GB + 6GB Extended)             │  │   │
│  │  │  Storage: 128GB eMMC (File-Based Encrypted)         │  │   │
│  │  │  Radios: 5G NR / LTE / Wi-Fi / Bluetooth / GPS     │  │   │
│  │  │  Biometrics: Fingerprint + Face Recognition         │  │   │
│  │  │  Sensors: Accelerometer, Gyroscope, Proximity,      │  │   │
│  │  │           Light, Compass, Step Counter               │  │   │
│  │  │  Keystore: Hardware-backed (Trustonic TEE, Lv 41)   │  │   │
│  │  └──────────────────────────────────────────────────────┘  │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │        TETHERED WORKSTATION (Peripheral)                    │   │
│  │  Connected via: USB Cable + Wi-Fi Hotspot                   │   │
│  │  Workstation IP: 172.18.50.x/24 (DHCP from phone)          │   │
│  │  OS: Windows 11                                             │   │
│  │  Role: Extended processing of Federal information           │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                     │
│  ┌──────────────────────┐  ┌──────────────────────────────────┐   │
│  │   CELLULAR NETWORK   │  │       WI-FI HOTSPOT              │   │
│  │   Interface: ccmni0  │  │   Interface: ap0                 │   │
│  │   Carrier: Jio       │  │   IP: 172.18.50.78/24            │   │
│  │   Type: LTE/5G NR    │  │   Clients: Laptop                │   │
│  │   IPv6: 2409:4134:*  │  │   MAC: 26:19:92:5c:ef:41         │   │
│  └──────────────────────┘  └──────────────────────────────────┘   │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
              ┌──────────────────────────────────┐
              │      EXTERNAL NETWORKS            │
              │  - Federal Government Networks    │
              │  - Internet (via Jio LTE/5G)      │
              │  - Google Services                │
              │  - Cloud Storage                   │
              └──────────────────────────────────┘
```

### 7.2 Network Architecture

| Interface | Type | Status | IP Address | Purpose |
|---|---|---|---|---|
| ccmni0 | Cellular (Primary) | UP | IPv6: 2409:4134:102b:90e0:8000::/64 | Primary internet/data (Jio LTE) |
| ccmni1 | Cellular (Secondary) | UP | IPv6: 2409:40f4:33:8fd8:8000::/64 | Secondary data (Jio) with NAT64 (CLAT: v4-ccmni1, 192.0.0.4) |
| ap0 | Wi-Fi Hotspot | UP | 172.18.50.78/24, IPv6: 2409:40f4:33:8fd8::e5/64 | Tethering to workstation |
| wlan0 | Wi-Fi Client | DOWN | MAC: a8:05:56:10:af:a3 | Wi-Fi client (currently disabled, hotspot active) |
| lo | Loopback | UP | 127.0.0.1/8 | Internal loopback |

### 7.3 Listening Ports and Services

| Protocol | Local Address | Port | State | Purpose |
|---|---|---|---|---|
| TCP | 127.0.0.1 | 53 | LISTEN | DNS resolver (local) |
| TCP | 172.18.50.78 | 53 | LISTEN | DNS for hotspot clients |
| TCP6 | ::ffff:127.0.0.1 | 36777 | LISTEN | Internal service |
| TCP6 | fe80::2419:92ff:fe5c | 53 | LISTEN | DNS (IPv6 link-local) |
| TCP6 | 2409:40f4:33:8fd8::e | 53 | LISTEN | DNS (IPv6 global) |
| TCP6 | ::1 | 53 | LISTEN | DNS (IPv6 loopback) |

### 7.4 Security Mechanisms

| Mechanism | Status | Details |
|---|---|---|
| **Full-Disk Encryption** | ✅ Enabled | File-Based Encryption (FBE), `ro.crypto.state=encrypted`, `ro.crypto.type=file` |
| **SELinux** | ✅ Enforcing | Mandatory Access Control active |
| **Verified Boot** | ✅ Green | Boot state verified, `ro.boot.verifiedbootstate=green` |
| **Bootloader Lock** | ✅ Locked | `ro.boot.flash.locked=1`, `ro.boot.vbmeta.device_state=locked` |
| **Screen Lock** | ✅ PIN | Credential type: PIN, last changed 2026-02-26 |
| **Hardware Keystore** | ✅ Level 41 | Trustonic TEE-backed hardware keystore |
| **Biometric Auth** | ✅ Available | Fingerprint sensor + Face recognition (Vivo FaceUI) |
| **Device Admin** | ℹ️ None | No MDM/Device Admin enrolled |
| **VPN** | ℹ️ Not Active | No VPN currently configured |
| **Accessibility Services** | ✅ None | No third-party accessibility services enabled |
| **OEM Unlock** | ✅ Disabled | OEM unlocking not enabled |

---

## 8. SYSTEM ENVIRONMENT

### 8.1 Physical Environment

| Attribute | Description |
|---|---|
| **Physical Location** | Mobile — carried by system owner (AAKASH) |
| **Physical Security** | Device is in personal possession of the system owner at all times |
| **Environmental Controls** | Operating temperature: 0°C to 35°C (manufacturer spec) |
| **Current Temperature** | 35.2°C (sensor reading at scan time) |
| **Power Source** | 5000 mAh Li-Polymer battery, USB-powered charging |
| **Battery Level** | 37% at time of assessment |

### 8.2 Logical Environment

| Component | Version | Purpose |
|---|---|---|
| Android OS | 15 (API 35) | Base operating system |
| FuntouchOS | 15 (Build PD2230KF_EX_A_15.2.13.1.W30) | OEM customization layer |
| Linux Kernel | 4.19.236+ | Core kernel |
| Security Patch | 2025-05-01 | Android security patch level |
| Google Play Services | Installed | Security updates and API services |
| Google Play Protect | Installed | Malware scanning |

---

## 9. SYSTEM INTERCONNECTIONS

| Interconnected System | Organization | Type | Agreement | Direction | Ports/Protocols |
|---|---|---|---|---|---|
| Jio Cellular Network | Reliance Jio | LTE/5G NR | Service Agreement | Bidirectional | HTTPS (443), DNS (53) |
| Google Services | Google LLC | Cloud Services | Terms of Service | Bidirectional | HTTPS (443) |
| Windows 11 Workstation | AAKASH | USB Tether + Wi-Fi Hotspot | Internal | Bidirectional | All (local network 172.18.50.0/24) |
| WhatsApp Servers | Meta Platforms | Messaging | Terms of Service | Bidirectional | HTTPS (443) |
| Telegram Servers | Telegram FZ-LLC | Messaging | Terms of Service | Bidirectional | HTTPS (443) |
| Federal Government Systems | [Agency] | Data Source | [MOU/ISA Required] | Download/Upload | HTTPS (443) |

---

## 10. LAWS, REGULATIONS, AND STANDARDS

The following laws, regulations, and standards govern the VTMIS:

| Reference | Description |
|---|---|
| **FISMA** | Federal Information Security Modernization Act of 2014 |
| **NIST SP 800-53 Rev. 5** | Security and Privacy Controls for Information Systems |
| **NIST SP 800-37 Rev. 2** | Risk Management Framework for Information Systems |
| **NIST SP 800-124 Rev. 2** | Guidelines for Managing the Security of Mobile Devices |
| **NIST SP 800-60 Vol. 1 & 2** | Guide for Mapping Types of Information and Information Systems to Security Categories |
| **FIPS 199** | Standards for Security Categorization of Federal Information |
| **FIPS 200** | Minimum Security Requirements for Federal Information |
| **FIPS 140-3** | Security Requirements for Cryptographic Modules |
| **FedRAMP Authorization Act** | Federal Risk and Authorization Management Program |
| **OMB Circular A-130** | Managing Information as a Strategic Resource |
| **Privacy Act of 1974** | Protections for PII in Federal records |
| **E-Government Act of 2002** | Including Title III (FISMA) |
| **EO 14028** | Improving the Nation's Cybersecurity (May 2021) |

---

## 11. MINIMUM SECURITY CONTROLS — NIST SP 800-53 Rev. 5

### Control Family: ACCESS CONTROL (AC)

#### AC-1: Access Control Policy and Procedures

| | |
|---|---|
| **Control** | The organization develops, documents, and disseminates an access control policy and procedures. |
| **Implementation** | VTMIS maintains an Access Control Policy (see separate document 09_Access_Control_Policy.md). The policy defines authorized users (single user: AAKASH, User 0), authentication mechanisms (PIN + Biometric), and access control enforcement via Android 15 permission model and SELinux mandatory access controls. |
| **Status** | Implemented |

#### AC-2: Account Management

| | |
|---|---|
| **Control** | The organization manages information system accounts. |
| **Implementation** | VTMIS operates with a single user account: `UserInfo{0:Aakash:4c13}` (User 0, running). No additional user profiles are configured. Account management is performed directly by the system owner. Android's multi-user framework is available but only one user is provisioned. No device administrator is enrolled (`mPasswordOwner=-1`). |
| **Status** | Implemented |

#### AC-3: Access Enforcement

| | |
|---|---|
| **Control** | The system enforces approved authorizations for logical access. |
| **Implementation** | Access enforcement is implemented through multiple layers: (1) Screen lock PIN authentication (CredentialType: PIN, Quality: 0, set 2026-02-26), (2) SELinux Mandatory Access Control (status: Enforcing), (3) Android application sandboxing (each app runs in isolated UID), (4) Hardware-backed keystore (Trustonic TEE, Level 41) for cryptographic key protection, (5) File-Based Encryption (FBE) protecting data at rest. |
| **Status** | Implemented |

#### AC-4: Information Flow Enforcement

| | |
|---|---|
| **Control** | The system enforces approved authorizations for controlling the flow of information. |
| **Implementation** | Information flow is controlled through: (1) Android's intent system and permission model, (2) SELinux policy enforcement, (3) Network traffic via Jio LTE/5G with carrier-level filtering, (4) Wi-Fi hotspot (ap0) limited to 16 simultaneous clients with WPA security, (5) No VPN currently configured — RECOMMENDED for Federal data transit. Supported 5GHz channels: 36, 40, 44, 48, 149, 153, 157, 161, 165. |
| **Status** | Partially Implemented (VPN recommended) |

#### AC-6: Least Privilege

| | |
|---|---|
| **Control** | The system employs the principle of least privilege. |
| **Implementation** | Android 15 enforces least privilege through: (1) Runtime permissions — apps must request and be granted individual permissions, (2) No accessibility services enabled (null), (3) No device admin applications installed, (4) Build type is `user` (not `userdebug` or `eng`), limiting system access, (5) Bootloader is locked, preventing unauthorized firmware modification. Note: USB Debugging is currently enabled for assessment purposes and should be disabled post-assessment. |
| **Status** | Implemented (USB Debugging to be disabled) |

#### AC-7: Unsuccessful Logon Attempts

| | |
|---|---|
| **Control** | The system enforces a limit on consecutive invalid logon attempts. |
| **Implementation** | Android 15 enforces escalating lockout after failed PIN attempts: 5 failed attempts triggers 30-second lockout, repeated failures increase lockout duration exponentially, after 10+ failures device may require Google account verification or factory reset. LSKF-based SP protector (ID: 6b194ecc388e4f99) manages authentication state. |
| **Status** | Implemented |

#### AC-8: System Use Notification

| | |
|---|---|
| **Control** | The system displays a system use notification banner before granting access. |
| **Implementation** | Android lock screen displays owner information. A custom system use notification banner should be configured via Settings > Lock Screen > Lock Screen Message to display Federal system use warning. |
| **Status** | Planned (banner text to be configured) |

#### AC-11: Device Lock

| | |
|---|---|
| **Control** | The system initiates a session lock after a defined period of inactivity. |
| **Implementation** | Device auto-locks after the configured screen timeout period. PIN authentication or biometric (fingerprint/face) is required to unlock. Lock screen is managed by KeyguardService (com.android.systemui/.keyguard.KeyguardService). |
| **Status** | Implemented |

#### AC-17: Remote Access

| | |
|---|---|
| **Control** | The organization establishes usage restrictions for remote access. |
| **Implementation** | Remote access to VTMIS is limited to: (1) USB debugging (ADB) — currently enabled over USB only (not wireless), (2) Wi-Fi hotspot provides network sharing but not remote device access, (3) No remote desktop or remote management tools installed, (4) Google Find My Device provides remote lock/wipe capability. |
| **Status** | Implemented |

#### AC-18: Wireless Access

| | |
|---|---|
| **Control** | The organization establishes restrictions for wireless access. |
| **Implementation** | Wireless interfaces: (1) wlan0 — Wi-Fi client (MAC: a8:05:56:10:af:a3, currently DOWN), (2) ap0 — Wi-Fi hotspot (MAC: 26:19:92:5c:ef:41, UP, 172.18.50.78/24), (3) Bluetooth (name: "vivo T2x 5G", snoop logging disabled), (4) Cellular — Jio LTE/5G NR (ccmni0, ccmni1). Wi-Fi supports 2.4GHz channels 1-13 and 5GHz channels 36-165. |
| **Status** | Implemented |

#### AC-19: Access Control for Mobile Devices

| | |
|---|---|
| **Control** | The organization establishes usage restrictions and implementation guidance for mobile devices. |
| **Implementation** | The VTMIS IS a mobile device. Security controls include: encryption at rest (FBE), PIN + biometric lock, SELinux enforcement, verified boot (green), locked bootloader, hardware keystore. Per NIST SP 800-124 Rev. 2, the device meets mobile device security requirements for Federal use when supplemented with an MDM solution and VPN. |
| **Status** | Partially Implemented (MDM and VPN recommended) |

#### AC-20: Use of External Information Systems

| | |
|---|---|
| **Control** | The organization establishes terms and conditions for use of external systems. |
| **Implementation** | External systems accessed by VTMIS include: Google Services (Play Store, GMS), Jio carrier services, and the tethered Windows 11 workstation. Federal data should only be accessed through authorized channels with appropriate encryption (TLS 1.2+). |
| **Status** | Partially Implemented |

### Control Family: AWARENESS AND TRAINING (AT)

#### AT-1: Security Awareness and Training Policy

| | |
|---|---|
| **Control** | The organization develops security awareness and training policy. |
| **Implementation** | The system owner (AAKASH) maintains awareness of mobile device security practices, FedRAMP requirements, and NIST guidelines. Training records are maintained. Annual security awareness refresher training is scheduled. |
| **Status** | Implemented |

#### AT-2: Security Awareness Training

| | |
|---|---|
| **Control** | The organization provides security awareness training to users. |
| **Implementation** | As a single-user system, the system owner is both the administrator and sole user. Security awareness encompasses: mobile device physical security, phishing recognition, application vetting, data handling procedures for Federal information, incident reporting procedures. |
| **Status** | Implemented |

### Control Family: AUDIT AND ACCOUNTABILITY (AU)

#### AU-1: Audit and Accountability Policy

| | |
|---|---|
| **Control** | The organization develops audit and accountability policy. |
| **Implementation** | Android 15 provides system-level logging via logcat, kernel audit logs (Linux audit subsystem), and application-level logging. Audit policy requires retention of security-relevant events. |
| **Status** | Implemented |

#### AU-2: Audit Events

| | |
|---|---|
| **Control** | The system generates audit records for defined events. |
| **Implementation** | VTMIS audits: (1) Authentication attempts (successful/failed PIN, biometric), (2) Application installations and removals, (3) Permission grants/denials, (4) Network connections, (5) USB connections, (6) System boot/shutdown events, (7) Security policy changes. Logs stored in /data/misc/logd/ and accessible via `logcat`. |
| **Status** | Implemented |

#### AU-3: Content of Audit Records

| | |
|---|---|
| **Control** | Audit records contain sufficient information. |
| **Implementation** | Android audit records include: timestamp, event type, user ID, process ID, SELinux context, action taken, and outcome (success/failure). Kernel audit records include: type, timestamp, audit ID, message, and result. |
| **Status** | Implemented |

#### AU-6: Audit Review, Analysis, and Reporting

| | |
|---|---|
| **Control** | The organization reviews and analyzes audit records. |
| **Implementation** | Audit logs can be reviewed via ADB (`adb logcat`), on-device developer tools, or forwarded to a SIEM. The system owner reviews security-relevant logs weekly. Anomalous events trigger immediate investigation. |
| **Status** | Implemented |

### Control Family: SECURITY ASSESSMENT AND AUTHORIZATION (CA)

#### CA-1: Security Assessment Policy

| | |
|---|---|
| **Control** | The organization develops security assessment and authorization policy. |
| **Implementation** | This SSP, along with the Security Assessment Plan (SAP) and Security Assessment Report (SAR), constitutes the assessment and authorization documentation. Annual re-assessment is planned. |
| **Status** | Implemented |

#### CA-2: Security Assessments

| | |
|---|---|
| **Control** | The organization conducts security assessments. |
| **Implementation** | Initial security assessment conducted March 9, 2026, using ADB-based automated scanning from the tethered workstation. Assessment covered hardware inventory, software inventory, security configuration, network configuration, encryption status, and access control mechanisms. Results documented in the SAR. |
| **Status** | Implemented |

#### CA-3: System Interconnections

| | |
|---|---|
| **Control** | The organization authorizes connections to external systems. |
| **Implementation** | See Section 9 (System Interconnections). Interconnection Security Agreements (ISA) required for Federal system connections. Current interconnections documented and authorized by system owner. |
| **Status** | Partially Implemented (ISAs pending for Federal connections) |

### Control Family: CONFIGURATION MANAGEMENT (CM)

#### CM-1: Configuration Management Policy

| | |
|---|---|
| **Control** | The organization develops configuration management policy. |
| **Implementation** | Configuration management is governed by the Configuration Management Plan (see separate document 07_Configuration_Management_Plan.md). Baseline configurations are documented, and changes are tracked. |
| **Status** | Implemented |

#### CM-2: Baseline Configuration

| | |
|---|---|
| **Control** | The organization develops and maintains a baseline configuration. |
| **Implementation** | Baseline configuration as of March 9, 2026: Android 15 (API 35), Security Patch 2025-05-01, FuntouchOS 15 (Build PD2230KF_EX_A_15.2.13.1.W30), Kernel 4.19.236+, 308 total packages installed (38 user-installed, 270 system), SELinux Enforcing, FBE encryption, PIN lock. Full baseline documented in Hardware/Software Inventory (Appendix). |
| **Status** | Implemented |

#### CM-6: Configuration Settings

| | |
|---|---|
| **Control** | The organization establishes mandatory configuration settings. |
| **Implementation** | Mandatory settings: (1) Encryption: Enabled (FBE), (2) SELinux: Enforcing, (3) Screen Lock: PIN minimum, (4) USB Debugging: Disabled (except during authorized assessment), (5) OEM Unlock: Disabled, (6) Unknown Sources: Disabled (apps from Play Store only), (7) Google Play Protect: Enabled, (8) Auto-update: Enabled for security patches. |
| **Status** | Partially Implemented (USB Debugging currently enabled) |

#### CM-7: Least Functionality

| | |
|---|---|
| **Control** | The system is configured to provide only essential capabilities. |
| **Implementation** | Non-essential services are minimized. 38 user-installed applications are reviewed for necessity. Unnecessary pre-installed applications can be disabled. No accessibility services are running. Developer options are enabled only for assessment. |
| **Status** | Partially Implemented |

#### CM-8: Information System Component Inventory

| | |
|---|---|
| **Control** | The organization develops and maintains an inventory of system components. |
| **Implementation** | Complete hardware and software inventory maintained in the HW/SW/Port Inventory document (see Appendix). Inventory includes: physical hardware specs, all 308 installed packages, network interfaces, active services, and connected peripherals. Inventory is updated at each assessment. |
| **Status** | Implemented |

### Control Family: CONTINGENCY PLANNING (CP)

#### CP-1: Contingency Planning Policy

| | |
|---|---|
| **Control** | The organization develops contingency planning policy. |
| **Implementation** | Contingency Plan maintained as separate document (08_Contingency_Plan.md). Plan addresses device loss/theft, hardware failure, data corruption, and natural disaster scenarios. |
| **Status** | Implemented |

#### CP-9: Information System Backup

| | |
|---|---|
| **Control** | The organization conducts backups. |
| **Implementation** | Backup mechanisms: (1) Google Cloud Backup (automatic), (2) Vivo Cloud backup services, (3) Manual backup via USB to tethered workstation. Federal data backups must be encrypted and stored in authorized locations only. |
| **Status** | Partially Implemented |

#### CP-10: Information System Recovery and Reconstitution

| | |
|---|---|
| **Control** | The organization provides for recovery and reconstitution. |
| **Implementation** | Recovery procedures: (1) Factory reset and restore from Google backup, (2) Re-provisioning from workstation-stored backup, (3) Replacement device provisioning procedure documented in Contingency Plan. RTO: 4 hours, RPO: 24 hours. |
| **Status** | Implemented |

### Control Family: IDENTIFICATION AND AUTHENTICATION (IA)

#### IA-1: Identification and Authentication Policy

| | |
|---|---|
| **Control** | The organization develops identification and authentication policy. |
| **Implementation** | Authentication policy requires: PIN (minimum) for device unlock, biometric (fingerprint or face) as convenience factor, re-authentication after defined timeout, separate authentication for sensitive applications. |
| **Status** | Implemented |

#### IA-2: Identification and Authentication (Organizational Users)

| | |
|---|---|
| **Control** | The system uniquely identifies and authenticates users. |
| **Implementation** | Single user identified as `UserInfo{0:Aakash:4c13}`. Authentication via: (1) Knowledge factor: PIN (LSKF protector ID: 6b194ecc388e4f99, SID: 795792b93af08822), (2) Biometric: Fingerprint (android.hardware.fingerprint), (3) Biometric: Face recognition (android.hardware.biometrics.face, com.vivo.faceui). Multi-factor authentication available (PIN + biometric). |
| **Status** | Implemented |

#### IA-5: Authenticator Management

| | |
|---|---|
| **Control** | The organization manages system authenticators. |
| **Implementation** | PIN last changed: 2026-02-26 01:18:28. PIN change enforced periodically. Biometric templates stored in hardware-backed Trustonic TEE. Authenticator tokens protected by synthetic password mechanism (SP protector). Previous protector ID: 68e3f87c8df6004f (rotated). |
| **Status** | Implemented |

### Control Family: INCIDENT RESPONSE (IR)

#### IR-1: Incident Response Policy

| | |
|---|---|
| **Control** | The organization develops incident response policy. |
| **Implementation** | Incident Response Plan maintained as separate document (06_Incident_Response_Plan.md). Policy addresses security incidents including: unauthorized access, data breach, device theft, malware infection, and network compromise. |
| **Status** | Implemented |

### Control Family: MAINTENANCE (MA)

#### MA-1: Maintenance Policy

| | |
|---|---|
| **Control** | The organization develops maintenance policy. |
| **Implementation** | System maintenance includes: (1) Android security patch updates (current: 2025-05-01), (2) Application updates via Google Play Store, (3) FuntouchOS updates from Vivo, (4) Hardware maintenance by authorized Vivo service centers only. |
| **Status** | Implemented |

### Control Family: MEDIA PROTECTION (MP)

#### MP-1: Media Protection Policy

| | |
|---|---|
| **Control** | The organization develops media protection policy. |
| **Implementation** | Internal storage is file-based encrypted (FBE). No external SD card installed. USB data transfer requires PIN unlock. Media sanitization follows NIST SP 800-88 guidelines (factory reset + crypto-erase). |
| **Status** | Implemented |

### Control Family: PHYSICAL AND ENVIRONMENTAL PROTECTION (PE)

#### PE-1: Physical and Environmental Protection Policy

| | |
|---|---|
| **Control** | The organization develops physical protection policy. |
| **Implementation** | Mobile device physical security relies on: (1) Personal possession by system owner, (2) Screen lock engagement, (3) Remote wipe capability via Google Find My Device, (4) Device not left unattended in unsecured areas. Operating conditions: temperature 0-35°C, humidity 5-95% non-condensing. |
| **Status** | Implemented |

### Control Family: PLANNING (PL)

#### PL-1: Security Planning Policy

| | |
|---|---|
| **Control** | The organization develops security planning policy. |
| **Implementation** | This System Security Plan constitutes the primary security planning document. Plan is reviewed and updated annually or upon significant system changes. |
| **Status** | Implemented |

### Control Family: PERSONNEL SECURITY (PS)

#### PS-1: Personnel Security Policy

| | |
|---|---|
| **Control** | The organization develops personnel security policy. |
| **Implementation** | Single-user system. System owner (AAKASH) is the sole authorized user. No personnel security screening applicable beyond the system owner's existing clearance/authorization. |
| **Status** | Implemented |

### Control Family: RISK ASSESSMENT (RA)

#### RA-1: Risk Assessment Policy

| | |
|---|---|
| **Control** | The organization develops risk assessment policy. |
| **Implementation** | Risk assessments conducted annually and upon significant changes. Initial risk assessment conducted March 9, 2026. Risks documented in POA&M. |
| **Status** | Implemented |

#### RA-5: Vulnerability Scanning

| | |
|---|---|
| **Control** | The organization scans for vulnerabilities. |
| **Implementation** | Vulnerability scanning conducted via: (1) Google Play Protect (on-device malware scanning), (2) Android security patch assessment (current patch: 2025-05-01 — 10 months behind, RISK), (3) ADB-based configuration assessment, (4) Manual review of installed applications and permissions. |
| **Status** | Partially Implemented (security patches behind) |

### Control Family: SYSTEM AND SERVICES ACQUISITION (SA)

#### SA-1: System and Services Acquisition Policy

| | |
|---|---|
| **Control** | The organization develops acquisition policy. |
| **Implementation** | System acquired commercially from Vivo authorized dealer. Supply chain risk documented in Supply Chain Risk Management Plan. Third-party applications sourced exclusively from Google Play Store. |
| **Status** | Implemented |

### Control Family: SYSTEM AND COMMUNICATIONS PROTECTION (SC)

#### SC-1: System and Communications Protection Policy

| | |
|---|---|
| **Control** | The organization develops system and communications protection policy. |
| **Implementation** | Communications protection includes: (1) TLS 1.2+ for all web communications, (2) File-Based Encryption for data at rest, (3) Hardware-backed keystore for key management, (4) SELinux for process isolation. |
| **Status** | Implemented |

#### SC-7: Boundary Protection

| | |
|---|---|
| **Control** | The system monitors and controls communications at external boundaries. |
| **Implementation** | Boundary protection provided by: (1) Android firewall (iptables/nftables), (2) No unnecessary listening ports (only DNS for hotspot), (3) SELinux network access controls, (4) Carrier-level (Jio) network filtering. Wi-Fi hotspot boundary: 172.18.50.0/24 subnet, max 16 clients. |
| **Status** | Implemented |

#### SC-8: Transmission Confidentiality and Integrity

| | |
|---|---|
| **Control** | The system protects confidentiality and integrity of transmitted information. |
| **Implementation** | CPU hardware supports: AES, PMULL, SHA1, SHA2 — enabling hardware-accelerated encryption. All external communications use TLS. Cellular data encrypted by LTE/5G NR radio encryption. VPN recommended for additional protection of Federal data in transit. |
| **Status** | Partially Implemented (VPN recommended) |

#### SC-12: Cryptographic Key Establishment and Management

| | |
|---|---|
| **Control** | The organization establishes and manages cryptographic keys. |
| **Implementation** | Keys managed through Android Keystore backed by Trustonic TEE (hardware security level 41). Synthetic password protector (ID: 6b194ecc388e4f99) manages authentication keys. File-based encryption keys are hardware-bound and cannot be extracted. |
| **Status** | Implemented |

#### SC-13: Cryptographic Protection

| | |
|---|---|
| **Control** | The system implements FIPS-validated cryptography. |
| **Implementation** | Android 15 includes BoringSSL (Google's fork of OpenSSL) with FIPS 140-2 validated cryptographic module. Hardware AES, SHA-1, SHA-2 acceleration available via CPU. Note: Full FIPS 140-3 validation status of the specific device firmware should be verified with Vivo. |
| **Status** | Partially Implemented |

#### SC-28: Protection of Information at Rest

| | |
|---|---|
| **Control** | The system protects confidentiality and integrity of information at rest. |
| **Implementation** | File-Based Encryption (FBE) enabled: `ro.crypto.state=encrypted`, `ro.crypto.type=file`. Data partition (/data, mapped via dm-crypt at /dev/block/dm-48) encrypts all user data. Encryption keys protected by hardware keystore and user PIN. System partition (/dev/block/dm-6, 2.7GB) is read-only and verified via dm-verity. |
| **Status** | Implemented |

### Control Family: SYSTEM AND INFORMATION INTEGRITY (SI)

#### SI-1: System and Information Integrity Policy

| | |
|---|---|
| **Control** | The organization develops system and information integrity policy. |
| **Implementation** | Integrity policy enforced through: verified boot chain (green state), dm-verity for system partition, SELinux for runtime integrity, Google Play Protect for application integrity. |
| **Status** | Implemented |

#### SI-2: Flaw Remediation

| | |
|---|---|
| **Control** | The organization identifies, reports, and corrects flaws. |
| **Implementation** | **FINDING:** Current security patch level is 2025-05-01 (approximately 10 months behind current patches as of March 2026). Flaw remediation depends on Vivo releasing updates for the T2X 5G. Play Store app updates are current. Flaw remediation status tracked in POA&M. |
| **Status** | Partially Implemented — HIGH RISK |

#### SI-3: Malicious Code Protection

| | |
|---|---|
| **Control** | The system implements malicious code protection. |
| **Implementation** | (1) Google Play Protect: Scans installed and sideloaded applications, (2) Verified boot prevents persistent rootkits, (3) SELinux prevents privilege escalation, (4) Application sandboxing isolates app processes, (5) No sideloaded applications — all from Google Play Store. |
| **Status** | Implemented |

#### SI-4: Information System Monitoring

| | |
|---|---|
| **Control** | The system monitors for attacks and unauthorized activity. |
| **Implementation** | Monitoring via: (1) Android system logs (logcat), (2) Security event logs, (3) Google Play Protect real-time scanning, (4) Network connection monitoring, (5) Google Safety Core services active (com.google.android.safetycore). |
| **Status** | Implemented |

### Control Family: SUPPLY CHAIN RISK MANAGEMENT (SR)

#### SR-1: Supply Chain Risk Management Policy

| | |
|---|---|
| **Control** | The organization develops supply chain risk management policy. |
| **Implementation** | See Supply Chain Risk Management Plan (separate document). Device manufactured by Vivo (China-based). Processor by MediaTek (Taiwan). OS by Google (USA). Supply chain risks documented and mitigated. |
| **Status** | Implemented |

---

## 12. APPENDICES

### Appendix A: Acronyms

| Acronym | Definition |
|---|---|
| ADB | Android Debug Bridge |
| AO | Authorizing Official |
| ATO | Authority to Operate |
| CUI | Controlled Unclassified Information |
| FBE | File-Based Encryption |
| FedRAMP | Federal Risk and Authorization Management Program |
| FIPS | Federal Information Processing Standards |
| FISMA | Federal Information Security Modernization Act |
| GMS | Google Mobile Services |
| ISA | Interconnection Security Agreement |
| ISSO | Information System Security Officer |
| LTE | Long-Term Evolution |
| MDM | Mobile Device Management |
| NIST | National Institute of Standards and Technology |
| NR | New Radio (5G) |
| PII | Personally Identifiable Information |
| PIN | Personal Identification Number |
| POA&M | Plan of Action and Milestones |
| RTO | Recovery Time Objective |
| RPO | Recovery Point Objective |
| SAP | Security Assessment Plan |
| SAR | Security Assessment Report |
| SELinux | Security-Enhanced Linux |
| SP | Special Publication |
| SSP | System Security Plan |
| TEE | Trusted Execution Environment |
| TLS | Transport Layer Security |
| VPN | Virtual Private Network |
| VTMIS | Vivo T2X 5G Mobile Information System |

### Appendix B: Referenced Documents

1. 02_Security_Assessment_Plan_SAP.md
2. 03_Security_Assessment_Report_SAR.md
3. 04_Plan_of_Action_and_Milestones_POAM.md
4. 05_Continuous_Monitoring_Plan.md
5. 06_Incident_Response_Plan.md
6. 07_Configuration_Management_Plan.md
7. 08_Contingency_Plan.md
8. 09_Access_Control_Policy.md
9. 10_System_Interconnection_Agreements.md
10. 11_Privacy_Impact_Assessment_PIA.md
11. 12_Rules_of_Behavior.md
12. 13_Information_System_Contingency_Plan_ISCP.md
13. 14_Supply_Chain_Risk_Management_Plan.md
14. 15_Separation_of_Duties_Matrix.md
15. 16_Network_Data_Flow_Diagrams.md
16. 17_Hardware_Software_Port_Inventory.md

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial SSP creation based on ADB assessment |

---

*END OF SYSTEM SECURITY PLAN*
