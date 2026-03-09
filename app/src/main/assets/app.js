// Document definitions
const DOCS = [
    { file: "README.md", title: "README — Package Overview", num: "00" },
    { file: "01_System_Security_Plan_SSP.md", title: "System Security Plan (SSP)", num: "01" },
    { file: "02_Security_Assessment_Plan_SAP.md", title: "Security Assessment Plan (SAP)", num: "02" },
    { file: "03_Security_Assessment_Report_SAR.md", title: "Security Assessment Report (SAR)", num: "03" },
    { file: "04_Plan_of_Action_and_Milestones_POAM.md", title: "Plan of Action & Milestones (POA&M)", num: "04" },
    { file: "05_Continuous_Monitoring_Plan.md", title: "Continuous Monitoring Plan", num: "05" },
    { file: "06_Incident_Response_Plan.md", title: "Incident Response Plan (IRP)", num: "06" },
    { file: "07_Configuration_Management_Plan.md", title: "Configuration Management Plan", num: "07" },
    { file: "08_Contingency_Plan.md", title: "Contingency Plan", num: "08" },
    { file: "09_Access_Control_Policy.md", title: "Access Control Policy", num: "09" },
    { file: "10_System_Interconnection_Agreements.md", title: "System Interconnection Agreements", num: "10" },
    { file: "11_Privacy_Impact_Assessment_PIA.md", title: "Privacy Impact Assessment (PIA)", num: "11" },
    { file: "12_Rules_of_Behavior_ROB.md", title: "Rules of Behavior (ROB)", num: "12" },
    { file: "13_Information_System_Contingency_Plan_ISCP.md", title: "Information System Contingency Plan (ISCP)", num: "13" },
    { file: "14_Supply_Chain_Risk_Management_Plan.md", title: "Supply Chain Risk Management Plan", num: "14" },
    { file: "15_Separation_of_Duties_Matrix.md", title: "Separation of Duties Matrix", num: "15" },
    { file: "16_Network_Data_Flow_Diagrams.md", title: "Network & Data Flow Diagrams", num: "16" },
    { file: "17_Hardware_Software_Port_Inventory.md", title: "Hardware/Software/Port Inventory", num: "17" }
];

let currentDoc = null;

// Initialize
document.addEventListener("DOMContentLoaded", function() {
    buildDocList();
    loadTheme();
});

function buildDocList() {
    var list = document.getElementById("docList");
    list.innerHTML = "";
    DOCS.forEach(function(doc, i) {
        var li = document.createElement("li");
        li.setAttribute("data-index", i);
        li.innerHTML = '<span class="doc-num">' + escapeHtml(doc.num) + '</span>' + escapeHtml(doc.title);
        li.onclick = function() { loadDoc(i); };
        list.appendChild(li);
    });
}

function filterDocs() {
    var query = document.getElementById("searchInput").value.toLowerCase();
    var items = document.querySelectorAll("#docList li");
    items.forEach(function(li) {
        var idx = parseInt(li.getAttribute("data-index"));
        var doc = DOCS[idx];
        var match = doc.title.toLowerCase().indexOf(query) !== -1 ||
                    doc.num.indexOf(query) !== -1 ||
                    doc.file.toLowerCase().indexOf(query) !== -1;
        li.style.display = match ? "" : "none";
    });
}

function loadDoc(index) {
    currentDoc = index;
    var doc = DOCS[index];

    // Update active state
    document.querySelectorAll("#docList li").forEach(function(li) {
        li.classList.remove("active");
    });
    var activeLi = document.querySelector('#docList li[data-index="' + index + '"]');
    if (activeLi) activeLi.classList.add("active");

    // Update title
    document.getElementById("docTitle").textContent = doc.title;

    // Close sidebar on mobile
    closeSidebar();

    // Fetch and render
    var content = document.getElementById("content");
    content.innerHTML = '<div style="text-align:center;padding:60px;color:var(--text-secondary);">Loading...</div>';

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "docs/" + doc.file, true);
    xhr.onload = function() {
        if (xhr.status === 200 || xhr.status === 0) {
            var html = renderMarkdown(xhr.responseText);
            content.innerHTML = '<div class="md-body">' + html + '</div>';
            content.scrollTop = 0;
        } else {
            content.innerHTML = '<div style="text-align:center;padding:60px;color:#e53935;">Failed to load document.</div>';
        }
    };
    xhr.onerror = function() {
        content.innerHTML = '<div style="text-align:center;padding:60px;color:#e53935;">Error loading document.</div>';
    };
    xhr.send();
}

// Markdown to HTML renderer
function renderMarkdown(md) {
    var lines = md.split("\n");
    var html = "";
    var inCodeBlock = false;
    var codeContent = "";
    var inTable = false;
    var tableRows = [];
    var inList = false;
    var listType = "";
    var listItems = [];
    var inBlockquote = false;
    var bqContent = [];

    function flushList() {
        if (!inList) return;
        html += "<" + listType + ">";
        listItems.forEach(function(item) { html += "<li>" + inlineFormat(item) + "</li>"; });
        html += "</" + listType + ">";
        inList = false;
        listItems = [];
    }

    function flushBlockquote() {
        if (!inBlockquote) return;
        html += "<blockquote>" + bqContent.map(function(l) { return "<p>" + inlineFormat(l) + "</p>"; }).join("") + "</blockquote>";
        inBlockquote = false;
        bqContent = [];
    }

    function flushTable() {
        if (!inTable) return;
        html += renderTable(tableRows);
        inTable = false;
        tableRows = [];
    }

    for (var i = 0; i < lines.length; i++) {
        var line = lines[i];

        // Code blocks
        if (line.match(/^```/)) {
            if (inCodeBlock) {
                html += "<pre><code>" + escapeHtml(codeContent) + "</code></pre>";
                codeContent = "";
                inCodeBlock = false;
            } else {
                flushList();
                flushBlockquote();
                flushTable();
                inCodeBlock = true;
            }
            continue;
        }

        if (inCodeBlock) {
            codeContent += (codeContent ? "\n" : "") + line;
            continue;
        }

        // Blank line
        if (line.trim() === "") {
            flushList();
            flushBlockquote();
            flushTable();
            continue;
        }

        // Horizontal rule
        if (line.match(/^(\-{3,}|\*{3,}|_{3,})\s*$/)) {
            flushList();
            flushBlockquote();
            flushTable();
            html += "<hr>";
            continue;
        }

        // Headings
        var headingMatch = line.match(/^(#{1,6})\s+(.+)/);
        if (headingMatch) {
            flushList();
            flushBlockquote();
            flushTable();
            var level = headingMatch[1].length;
            var text = headingMatch[2].replace(/\s*#{1,6}\s*$/, "");
            var id = text.toLowerCase().replace(/[^\w\s-]/g, "").replace(/\s+/g, "-");
            html += "<h" + level + ' id="' + id + '">' + inlineFormat(text) + "</h" + level + ">";
            continue;
        }

        // Table
        if (line.indexOf("|") !== -1 && line.trim().startsWith("|")) {
            if (!inTable) {
                flushList();
                flushBlockquote();
                inTable = true;
                tableRows = [];
            }
            // Skip separator line
            if (line.match(/^\|[\s\-:|]+\|$/)) {
                continue;
            }
            tableRows.push(line);
            continue;
        } else if (inTable) {
            flushTable();
        }

        // Blockquote
        if (line.match(/^>\s?/)) {
            flushList();
            flushTable();
            inBlockquote = true;
            bqContent.push(line.replace(/^>\s?/, ""));
            continue;
        } else if (inBlockquote) {
            flushBlockquote();
        }

        // Unordered list
        if (line.match(/^\s*[-*+]\s+/)) {
            flushBlockquote();
            flushTable();
            if (!inList || listType !== "ul") {
                flushList();
                inList = true;
                listType = "ul";
            }
            listItems.push(line.replace(/^\s*[-*+]\s+/, ""));
            continue;
        }

        // Ordered list
        if (line.match(/^\s*\d+\.\s+/)) {
            flushBlockquote();
            flushTable();
            if (!inList || listType !== "ol") {
                flushList();
                inList = true;
                listType = "ol";
            }
            listItems.push(line.replace(/^\s*\d+\.\s+/, ""));
            continue;
        }

        // Flush any open lists if we're here
        flushList();
        flushBlockquote();
        flushTable();

        // Paragraph
        html += "<p>" + inlineFormat(line) + "</p>";
    }

    // Flush remaining
    flushList();
    flushBlockquote();
    flushTable();
    if (inCodeBlock) {
        html += "<pre><code>" + escapeHtml(codeContent) + "</code></pre>";
    }

    return html;
}

function renderTable(rows) {
    if (rows.length === 0) return "";
    var result = '<table><thead><tr>';
    var headerCells = parseTableRow(rows[0]);
    headerCells.forEach(function(cell) {
        result += "<th>" + inlineFormat(cell) + "</th>";
    });
    result += "</tr></thead><tbody>";
    for (var i = 1; i < rows.length; i++) {
        var cells = parseTableRow(rows[i]);
        result += "<tr>";
        cells.forEach(function(cell) {
            result += "<td>" + inlineFormat(cell) + "</td>";
        });
        result += "</tr>";
    }
    result += "</tbody></table>";
    return result;
}

function parseTableRow(line) {
    var trimmed = line.trim();
    if (trimmed.startsWith("|")) trimmed = trimmed.substring(1);
    if (trimmed.endsWith("|")) trimmed = trimmed.substring(0, trimmed.length - 1);
    return trimmed.split("|").map(function(c) { return c.trim(); });
}

function inlineFormat(text) {
    // Escape HTML first
    text = escapeHtml(text);

    // Images: ![alt](url)
    text = text.replace(/!\[([^\]]*)\]\(([^)]+)\)/g, '<img src="$2" alt="$1" style="max-width:100%">');

    // Links: [text](url)
    text = text.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2">$1</a>');

    // Bold+italic: ***text*** or ___text___
    text = text.replace(/\*{3}(.+?)\*{3}/g, "<strong><em>$1</em></strong>");
    text = text.replace(/_{3}(.+?)_{3}/g, "<strong><em>$1</em></strong>");

    // Bold: **text** or __text__
    text = text.replace(/\*{2}(.+?)\*{2}/g, "<strong>$1</strong>");
    text = text.replace(/_{2}(.+?)_{2}/g, "<strong>$1</strong>");

    // Italic: *text* or _text_
    text = text.replace(/\*(.+?)\*/g, "<em>$1</em>");
    text = text.replace(/_(.+?)_/g, "<em>$1</em>");

    // Inline code: `code`
    text = text.replace(/`([^`]+)`/g, "<code>$1</code>");

    // Strikethrough: ~~text~~
    text = text.replace(/~~(.+?)~~/g, "<del>$1</del>");

    return text;
}

function escapeHtml(str) {
    return str
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// Sidebar toggle
function toggleSidebar() {
    document.getElementById("sidebar").classList.toggle("open");
    document.getElementById("sidebar-overlay").classList.toggle("open");
}

function closeSidebar() {
    document.getElementById("sidebar").classList.remove("open");
    document.getElementById("sidebar-overlay").classList.remove("open");
}

// Theme
function toggleTheme() {
    var isDark = document.documentElement.getAttribute("data-theme") === "dark";
    document.documentElement.setAttribute("data-theme", isDark ? "light" : "dark");
    localStorage.setItem("vtmis-theme", isDark ? "light" : "dark");
    document.getElementById("themeBtn").textContent = isDark ? "\u263E" : "\u2600";
}

function loadTheme() {
    var saved = localStorage.getItem("vtmis-theme");
    if (saved === "dark") {
        document.documentElement.setAttribute("data-theme", "dark");
        document.getElementById("themeBtn").textContent = "\u2600";
    }
}
