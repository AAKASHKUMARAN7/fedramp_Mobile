# HARDWARE, SOFTWARE, AND PORT INVENTORY

## Vivo T2X 5G Mobile Information System (VTMIS)

---

| **Document Information** | |
|---|---|
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **System Identifier** | VTMIS-2026-001 |
| **Document Version** | 1.0 |
| **Date** | March 9, 2026 |
| **Prepared By** | AAKASH |
| **Data Source** | ADB scan conducted March 9, 2026 |
| **Regulatory Basis** | NIST SP 800-53 Rev. 5: CM-8 (Information System Component Inventory) |

---

## 1. HARDWARE INVENTORY

### 1.1 Primary Device

| Attribute | Value | Source |
|---|---|---|
| **Manufacturer** | vivo | `ro.product.manufacturer` |
| **Brand** | vivo | `ro.product.brand` |
| **Model Number** | V2312 | `ro.product.model` |
| **Device Code** | V2225 | `ro.product.device` |
| **Commercial Name** | Vivo T2x 5G | Bluetooth name / marketing |
| **Serial Number** | 10BDC81HTA000Z4 | `ro.serialno` |
| **Build Number** | PD2230KF_EX_A_15.2.13.1.W30 | `ro.build.display.id` |
| **Build Type** | user (production) | `ro.build.type` |
| **Build Fingerprint** | vivo/V2225C/V2225:15/AP3A.241105.008 | `ro.build.fingerprint` |
| **Country/Region** | India (IN) | `ro.product.locale` |

### 1.2 System-on-Chip (SoC)

| Attribute | Value |
|---|---|
| **Chipset** | MediaTek Dimensity 6020 |
| **Platform Code** | mt6833 |
| **Architecture** | ARMv8.2-A (64-bit) |
| **CPU Cores** | 8 (octa-core) |
| **CPU Features** | fp, asimd, evtstrm, aes, pmull, sha1, sha2, crc32, atomics |
| **Hardware Crypto** | AES (hardware), SHA-1 (hardware), SHA-2 (hardware) |
| **GPU** | ARM Mali-G57 (integrated) |
| **ISP** | MediaTek Imagiq |
| **Modem** | Integrated 5G NR / LTE modem |

### 1.3 Memory

| Attribute | Value |
|---|---|
| **Total RAM** | 5,755,288 KB (~5.49 GB usable) |
| **Physical RAM** | 6 GB LPDDR4X |
| **Extended RAM** | +6 GB (virtual, from storage swap) |
| **Effective RAM** | 6 + 6 = 12 GB (advertised) |

### 1.4 Storage

| Partition | Total Size | Used | Available | Utilization |
|---|---|---|---|---|
| /data (user) | 106 GB | 103 GB | ~4 GB | **97%** |
| /system | Vendor-managed | — | — | Read-only (dm-verity) |
| /vendor | Vendor-managed | — | — | Read-only (dm-verity) |
| /boot | — | — | — | Verified Boot protected |
| /recovery | — | — | — | Recovery mode |

### 1.5 Display

| Attribute | Value |
|---|---|
| **Resolution** | 1080 × 2408 pixels |
| **Density** | 440 PPI (xxhdpi) |
| **Panel Type** | IPS LCD (estimated) |
| **Refresh Rate** | 120 Hz (estimated from chipset specs) |

### 1.6 Battery

| Attribute | Value |
|---|---|
| **Technology** | Li-poly (Lithium Polymer) |
| **Capacity** | 5000 mAh (typical for T2X 5G) |
| **Charge Level (at scan)** | 37% |
| **Power Source (at scan)** | USB powered |
| **Health** | Not reported (assumed good) |

### 1.7 Sensors and Peripherals

| Component | Type | Security Relevance |
|---|---|---|
| **Fingerprint Sensor** | Capacitive (side-mounted) | Biometric authentication |
| **Camera (Rear)** | Multi-lens | Photo/video with EXIF metadata |
| **Camera (Front)** | Single | Selfie, potential facial recognition |
| **GPS** | A-GPS, GLONASS, Galileo, BeiDou | Location tracking |
| **Accelerometer** | MEMS | Motion detection |
| **Proximity Sensor** | IR | Screen control |
| **Ambient Light Sensor** | Photodiode | Display brightness |
| **Gyroscope** | MEMS | Orientation (if present) |
| **SIM Tray** | Dual nano-SIM + microSD (shared) | Dual carrier capability |
| **USB Port** | USB Type-C | Data transfer, charging, ADB |
| **Speaker** | Mono/Stereo | Audio output |
| **Microphone** | Primary + noise cancellation | Audio input |
| **3.5mm Jack** | Present | Audio I/O |

### 1.8 Radio Interfaces

| Radio | Standard | Bands | Status |
|---|---|---|---|
| **5G NR** | Sub-6 GHz | n1/n3/n5/n8/n28/n40/n77/n78 (typical) | Capable |
| **LTE** | Cat 7+ | Multiple FDD/TDD bands (Jio compatible) | Active |
| **Wi-Fi** | 802.11 a/b/g/n/ac (Wi-Fi 5) | 2.4 GHz + 5 GHz | Active (hotspot) |
| **Bluetooth** | 5.1 | — | Active |
| **NFC** | — | — | Not confirmed |

---

## 2. SOFTWARE INVENTORY

### 2.1 Operating System

| Component | Version | Details |
|---|---|---|
| **Android Version** | 15 | API Level 35 |
| **Skin/Overlay** | FuntouchOS 15 | Vivo customization |
| **Build ID** | PD2230KF_EX_A_15.2.13.1.W30 | Production build |
| **Linux Kernel** | 4.19.236+ | Modified by Vivo |
| **Security Patch** | 2025-05-01 | ~10 months outdated (as of March 2026) |
| **SELinux** | Enforcing | Mandatory access control |
| **Verified Boot** | Green (verified) | OEM-signed boot chain |
| **Encryption** | File-Based Encryption (FBE) | CE + DE modes |
| **Bootloader** | Locked | Cannot flash unsigned images |
| **TEE** | Trustonic Kinibi | Hardware keystore Level 41 |

### 2.2 User-Installed Applications (38 Total)

| # | Package Name | App Name | Category | Risk Level | Notes |
|---|---|---|---|---|---|
| 1 | `com.whatsapp` | WhatsApp | Messaging | Moderate | E2E encrypted; NOT for Federal data |
| 2 | `org.telegram.messenger` | Telegram | Messaging | Moderate | E2E only in Secret Chats |
| 3 | `com.instagram.android` | Instagram | Social Media | Moderate | PII exposure (photos, location) |
| 4 | `com.twitter.android` | X (Twitter) | Social Media | Low | Public content primarily |
| 5 | `com.snapchat.android` | Snapchat | Social Media | Moderate | Camera/location access |
| 6 | `com.sbi.lotusintouch` | SBI YONO | Banking | High | Financial PII; session-based |
| 7 | `org.digilocker.app2` | DigiLocker | Government | High | Government IDs (Aadhaar, PAN) |
| 8 | `com.google.android.apps.maps` | Google Maps | Navigation | Low | Location data |
| 9 | `com.google.android.youtube` | YouTube | Entertainment | Low | Google account linked |
| 10 | `com.spotify.music` | Spotify | Entertainment | Low | Account data |
| 11 | `com.netflix.mediaclient` | Netflix | Entertainment | Low | Account data |
| 12 | `com.amazon.mShop.android.shopping` | Amazon Shopping | E-commerce | Moderate | Payment/address PII |
| 13 | `com.flipkart.android` | Flipkart | E-commerce | Moderate | Payment/address PII |
| 14 | `in.swiggy.android` | Swiggy | Food Delivery | Low | Address PII |
| 15 | `com.app.zomato` | Zomato | Food Delivery | Low | Address PII |
| 16 | `com.phonepe.app` | PhonePe | UPI Payment | High | Financial PII |
| 17 | `net.one97.paytm` | Paytm | UPI Payment | High | Financial PII |
| 18 | `com.google.android.apps.nbu.paisa.user` | Google Pay | UPI Payment | High | Financial PII |
| 19 | `com.openai.chatgpt` | ChatGPT | AI Assistant | Moderate | Conversation data may contain PII |
| 20 | `com.microsoft.bing` | Microsoft Copilot/Bing | AI Assistant | Moderate | Search/conversation data |
| 21 | `com.google.android.apps.bard` | Google Gemini | AI Assistant | Moderate | Google account linked |
| 22 | `com.linkedin.android` | LinkedIn | Professional | Moderate | Professional PII |
| 23 | `com.discord` | Discord | Communication | Moderate | Voice/text communication |
| 24 | `com.canva.editor` | Canva | Design | Low | Account data |
| 25 | `com.adobe.lrmobile` | Lightroom | Photo Editing | Low | Photo access |
| 26 | `com.mi.android.globalFileexplorer` | File Manager | Utility | Moderate | Full storage access |
| 27 | `com.shareit.lite` | SHAREit | File Transfer | **High** | Network file sharing; known security issues |
| 28 | `org.mozilla.firefox` | Firefox | Browser | Moderate | Browsing data, credentials |
| 29 | `com.brave.browser` | Brave Browser | Browser | Low | Privacy-focused |
| 30 | `com.microsoft.office.outlook` | Outlook | Email | Moderate | Email content, contacts |
| 31 | `com.jio.media.jiobeats` | JioSaavn | Entertainment | Low | Jio account linked |
| 32 | `com.jio.jioplay.tv` | JioTV | Entertainment | Low | Jio account linked |
| 33 | `com.jio.myjio` | MyJio | Carrier Mgmt | Low | Account management |
| 34 | `com.truecaller` | Truecaller | Caller ID | **High** | Uploads contacts to cloud |
| 35 | `com.google.android.keep` | Google Keep | Notes | Moderate | May contain sensitive notes |
| 36 | `com.google.android.apps.docs` | Google Docs | Productivity | Moderate | Document access |
| 37 | `com.google.android.apps.meetings` | Google Meet | Video Conf | Low | Communication |
| 38 | `com.zoom.videomeetings` | Zoom | Video Conf | Moderate | Communication; past security issues |

### 2.3 Critical System Packages (Selected from 270)

| Package Name | Description | Security Role |
|---|---|---|
| `com.android.providers.contacts` | Contacts provider | PII storage |
| `com.android.providers.telephony` | Telephony database | Call/SMS logs |
| `com.android.providers.media` | Media storage | Photo/video management |
| `com.android.settings` | System settings | Configuration management |
| `com.android.keychain` | System keychain | Certificate management |
| `com.google.android.gms` | Google Play Services | Google framework, FCM, location |
| `com.google.android.gsf` | Google Services Framework | Google API framework |
| `com.android.vending` | Google Play Store | App distribution |
| `com.google.android.gms.policy_sidecar_aps` | Play Protect | Anti-malware scanning |
| `com.android.chrome` | Chrome Browser | Pre-installed browser |
| `com.vivo.browser` | Vivo Browser | Vivo pre-installed browser |
| `com.android.bluetooth` | Bluetooth service | Bluetooth management |
| `com.android.nfc` | NFC service (if present) | NFC management |
| `com.mediatek.ims` | IMS (VoLTE/VoWiFi) | Voice over LTE |
| `com.vivo.fingerprint` | Fingerprint service | Biometric authentication |

### 2.4 Software Risk Summary

| Risk Level | Count | Percentage | Action |
|---|---|---|---|
| High | 5 | 13.2% | Enhanced monitoring; consider removal |
| Moderate | 18 | 47.4% | Regular permission review |
| Low | 15 | 39.5% | Standard monitoring |
| **Total** | **38** | **100%** | |

**High-Risk Apps Requiring Action:**
1. **SHAREit** — Known security vulnerabilities; enables file sharing over local network; recommend removal
2. **Truecaller** — Uploads contact database to cloud; privacy risk; recommend removal or restrict
3. **SBI YONO** — Legitimate but handles financial PII; ensure session isolation
4. **PhonePe** — Financial PII; transaction security critical
5. **Paytm** — Financial PII; transaction security critical

---

## 3. SECURITY CONFIGURATION INVENTORY

### 3.1 Security Feature Status

| Feature | Status | Expected | Compliant? |
|---|---|---|---|
| Device Encryption | File-Based Encryption (Active) | Encrypted | **YES** |
| SELinux | Enforcing | Enforcing | **YES** |
| Verified Boot | Green | Green | **YES** |
| Bootloader | Locked | Locked | **YES** |
| Screen Lock | PIN (set) | PIN or better | **YES** |
| Biometric | Fingerprint (enrolled) | Recommended | **YES** |
| USB Debugging | **Enabled** | **Disabled** | **NO** ⚠️ |
| Developer Options | **Enabled** | **Disabled** | **NO** ⚠️ |
| Play Protect | Active | Active | **YES** |
| Auto-update (apps) | Enabled | Enabled | **YES** |
| Find My Device | Active | Active | **YES** |
| VPN | **Not configured** | **Active** | **NO** ⚠️ |
| MDM | **Not enrolled** | **Enrolled** | **NO** ⚠️ |
| Work Profile | **Not configured** | Configured | **NO** ⚠️ |

### 3.2 Cryptographic Inventory

| Algorithm | Usage | Implementation | FIPS Status |
|---|---|---|---|
| AES-256-XTS | File-Based Encryption | Hardware (ARMv8 crypto) | FIPS 140-2 equivalent |
| AES-256-GCM | TLS connections | BoringSSL (Android) | FIPS 140-2 validated (Google) |
| SHA-256 | Integrity verification | Hardware + software | FIPS 180-4 |
| RSA-2048+ | TLS certificates | BoringSSL | FIPS 186-4 |
| ECDSA P-256 | Digital signatures | Hardware keystore (TEE) | FIPS 186-4 |
| HMAC-SHA256 | Message authentication | Android Keystore | FIPS 198-1 |
| 128-EEA (LTE) | Radio layer encryption | Baseband modem | 3GPP standard |
| WPA2-CCMP | Wi-Fi hotspot | Wi-Fi chipset | IEEE 802.11i |

---

## 4. NETWORK PORT INVENTORY

### 4.1 Listening Services

| Port | Protocol | Address | Service | State | Risk |
|---|---|---|---|---|---|
| 53 | UDP | 172.18.50.78 | dnsmasq (hotspot DNS) | LISTEN | Low (local only) |
| 5037 | TCP | 127.0.0.1 (USB) | ADB daemon | LISTEN (when enabled) | **HIGH** (disable!) |

### 4.2 Common Outbound Connections

| Destination | Port | Protocol | Purpose | Frequency |
|---|---|---|---|---|
| play.googleapis.com | 443 | HTTPS | Play Store, Play Protect | Continuous |
| fcm.googleapis.com | 5228 | TCP/TLS | Firebase Cloud Messaging | Persistent |
| mtalk.google.com | 5228 | TCP/TLS | Google Talk/FCM | Persistent |
| www.googleapis.com | 443 | HTTPS | Google APIs | Frequent |
| android.googleapis.com | 443 | HTTPS | Android services | Frequent |
| connectivitycheck.gstatic.com | 443 | HTTPS | Internet connectivity check | On connect |
| web.whatsapp.com | 443 | WSS | WhatsApp messaging | When active |
| api.telegram.org | 443 | HTTPS | Telegram API | When active |
| edge-mqtt.facebook.com | 443 | MQTT/TLS | WhatsApp/Instagram push | Persistent |
| *.sbi.co.in | 443 | HTTPS | SBI YONO | When active |
| api.digilocker.gov.in | 443 | HTTPS | DigiLocker | When active |

### 4.3 Blocked/Restricted Ports

| Port | Protocol | Restriction | Reason |
|---|---|---|---|
| 22 | TCP | No SSH server installed | Not needed |
| 23 | TCP | No Telnet | Security risk |
| 80 | TCP (outbound) | Allowed but minimal use | Prefer HTTPS |
| 21 | TCP | No FTP | Security risk |
| 445 | TCP | No SMB | Not applicable |
| 3389 | TCP | No RDP | Not applicable |

---

## 5. USER AND ACCOUNT INVENTORY

### 5.1 Device Users

| User ID | Username | Type | Status | Privileges |
|---|---|---|---|---|
| 0 | Aakash | Primary (Owner) | Active | Full device admin |
| (no others) | — | — | — | Single-user device |

### 5.2 Service Accounts

| Account | Service | Authentication | Last Verified |
|---|---|---|---|
| Google Account | Google/Android services | Password + 2FA | Active (syncing) |
| Jio Account | Carrier services | SIM-based + app PIN | Active |
| WhatsApp | Messaging | Phone number + PIN | Active |
| Telegram | Messaging | Phone number + 2FA | Active |
| SBI YONO | Banking | MPIN + biometric | Active |
| DigiLocker | Government docs | Aadhaar-linked + PIN | Active |
| Instagram | Social media | Password | Active |

---

## 6. PHYSICAL INVENTORY

### 6.1 Device and Accessories

| # | Item | Description | Serial/ID | Location | Status |
|---|---|---|---|---|---|
| INV-001 | Vivo T2X 5G | Primary mobile device | 10BDC81HTA000Z4 | AAKASH possession | Active |
| INV-002 | Jio SIM Card | Nano-SIM (Slot 1) | ICCID: [redacted] | In device | Active |
| INV-003 | USB Type-C Cable | Charging and data | N/A | With device | Active |
| INV-004 | Charger (OEM) | Vivo 18W/44W charger | N/A | With device | Active |
| INV-005 | Protective Case | Device case (if used) | N/A | On device | Active (if applicable) |
| INV-006 | Screen Protector | Tempered glass (if used) | N/A | On device | Active (if applicable) |

### 6.2 Connected Peripheral

| # | Item | Description | Connection | Status |
|---|---|---|---|---|
| PERIPH-001 | Windows 11 Laptop | Tethered workstation | Wi-Fi hotspot + USB | Active |

---

## 7. INVENTORY MANAGEMENT

### 7.1 Change Tracking

| Change Type | Process | Approval | Documentation |
|---|---|---|---|
| New app installed | Play Store auto-logged | User decision | Auto in Play Store history |
| App removed | Play Store auto-logged | User decision | Auto in Play Store history |
| Hardware replacement | Procurement + setup | System Owner | Update this inventory |
| OS update | OTA verified | System Owner | Update Section 2.1 |
| Security patch | OTA verified | System Owner | Update Section 2.1 |
| New peripheral connected | Per ISA-001 | System Owner | Update Section 6.2 |

### 7.2 Review Schedule

| Activity | Frequency | Responsible |
|---|---|---|
| Full inventory review | Quarterly | AAKASH |
| App inventory verification | Monthly | AAKASH |
| Hardware condition check | Monthly | AAKASH |
| Port scan verification | Quarterly | AAKASH |
| Account audit | Quarterly | AAKASH |

### 7.3 Verification Commands (ADB)

```bash
# Hardware verification
adb shell getprop ro.product.model
adb shell getprop ro.serialno
adb shell getprop ro.product.board

# OS version verification
adb shell getprop ro.build.version.release
adb shell getprop ro.build.version.security_patch
adb shell getprop ro.build.display.id

# App inventory
adb shell pm list packages -3          # User-installed apps
adb shell pm list packages             # All packages
adb shell pm list packages | wc -l     # Total count

# Storage check
adb shell df -h /data

# Network interfaces
adb shell ip addr show

# Listening ports
adb shell ss -tlnp
adb shell ss -ulnp

# Security check
adb shell getprop ro.crypto.state
adb shell getenforce
adb shell getprop ro.boot.verifiedbootstate

# User accounts
adb shell pm list users
```

---

## 8. INVENTORY CERTIFICATION

I certify that this inventory is accurate and complete as of the date of the ADB scan (March 9, 2026).

| Role | Name | Signature | Date |
|---|---|---|---|
| System Owner | AAKASH | _________________ | __________ |
| ISSM | AAKASH | _________________ | __________ |

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial Hardware/Software/Port Inventory |

---

*END OF HARDWARE, SOFTWARE, AND PORT INVENTORY*
