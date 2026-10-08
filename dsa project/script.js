// ============================================================
// SMART PATTERN SEARCH
// FRONTEND + JAVA BACKEND
// ============================================================

const input =
    document.getElementById("searchInput");

const searchBtn =
    document.getElementById("searchBtn") ||
    document.getElementById("searchButton");

const statusEl =
    document.getElementById("status");

const API =
    "http://localhost:8080/api/search?q=";


// ============================================================
// FIND EXISTING "READY TO ANALYZE" BOX
// ============================================================

function getAnalysisBox() {

    // Main existing result/empty box
    const emptyState =
        document.getElementById("emptyState");

    if (emptyState) {
        return emptyState;
    }


    // Other possible IDs
    const possibleIds = [
        "analysisBox",
        "readyState",
        "searchAnalysis"
    ];


    for (const id of possibleIds) {

        const element =
            document.getElementById(id);

        if (element) {
            return element;
        }
    }


    // Fallback:
    // Find the element containing "Ready to Analyze"
    const elements =
        document.querySelectorAll(
            "div, section, article"
        );


    for (const element of elements) {

        if (
            element.textContent &&
            element.textContent
                .trim()
                .includes("Ready to Analyze")
        ) {

            return element;
        }
    }


    return null;
}


// ============================================================
// TITLE CASE
// ============================================================

function titleCase(value) {

    return String(value || "")
        .toLowerCase()
        .replace(
            /\b\w/g,
            char => char.toUpperCase()
        );
}


// ============================================================
// ESCAPE HTML
// ============================================================

function escapeHTML(value) {

    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


// ============================================================
// SCROLL TO SAME ANALYSIS BOX
// ============================================================

function scrollToAnalysis() {

    const box =
        getAnalysisBox();


    if (!box) {
        return;
    }


    box.scrollIntoView({
        behavior: "smooth",
        block: "center"
    });
}


// ============================================================
// SHOW LOADING INSIDE SAME BOX
// ============================================================

function showLoading() {

    const box =
        getAnalysisBox();


    if (!box) {
        return;
    }


    box.innerHTML = `

        <div class="analysis-loading">

            <div class="analysis-icon">
                ✦
            </div>

            <h3>
                Analyzing "${escapeHTML(
                    input.value.trim()
                )}"
            </h3>

            <p>
                Running KMP, Edit Distance,
                BST and TF-IDF...
            </p>

        </div>

    `;
}


// ============================================================
// HIGHLIGHT RESULT
// ============================================================

function highlightResult() {

    const box =
        getAnalysisBox();


    if (!box) {
        return;
    }


    // Remove previous highlight
    box.classList.remove(
        "search-result-highlight"
    );


    // Restart CSS animation
    void box.offsetWidth;


    // Add highlight
    box.classList.add(
        "search-result-highlight"
    );
}


// ============================================================
// KMP RESULT
// ============================================================

function buildKMP(kmp, query) {

    kmp = kmp || {};


    const found =
        kmp.found === true;


    const badgeClass =
        found
            ? "result-green"
            : "result-red";


    const badgeText =
        found
            ? "FOUND"
            : "NOT FOUND";


    const message =
        kmp.message ||
        (
            found
                ? "Exact topic found using KMP."
                : "Exact topic not found using KMP."
        );


    const position =
        (
            kmp.position !== undefined &&
            kmp.position !== null
        )
            ? kmp.position
            : "—";


    return `

        <div class="result-module">

            <div class="module-name">

                <span>01</span>

                KMP

            </div>


            <div class="module-value">

                <strong class="${badgeClass}">
                    ${badgeText}
                </strong>

                <small>
                    ${escapeHTML(message)}
                    ${
                        found
                            ? ` · Position: ${position}`
                            : ""
                    }
                </small>

            </div>

        </div>

    `;
}


// ============================================================
// FUZZY MATCH + EDIT DISTANCE
// ============================================================

function buildFuzzy(fuzzy, kmp, query) {

    fuzzy = fuzzy || {};


    const closestTopic =
        fuzzy.topic ||
        query;


    const distance =
        (
            fuzzy.distance !== undefined &&
            fuzzy.distance !== null
        )
            ? fuzzy.distance
            : "—";


    const exact =
        kmp &&
        kmp.found === true;


    return `

        <div class="result-module">

            <div class="module-name">

                <span>02</span>

                FUZZY MATCH

            </div>


            <div class="module-value">

                <strong>
                    ${escapeHTML(
                        titleCase(
                            closestTopic
                        )
                    )}
                </strong>

                <small>
                    ${
                        exact
                            ? "Exact match · Edit Distance = 0"
                            : "Closest match found using Dynamic Programming"
                    }
                </small>


                <div class="edit-distance-value">

                    Edit Distance:

                    <b>
                        ${escapeHTML(
                            distance
                        )}
                    </b>

                </div>

            </div>

        </div>

    `;
}


// ============================================================
// BST RESULT
// ============================================================

function buildBST(bst, fuzzy, query) {

    bst = bst || {};
    fuzzy = fuzzy || {};


    const topic =
        bst.topic ||
        fuzzy.topic ||
        query;


    const found =
        bst.found === true;


    const file =
        bst.file ||
        "No file";


    const index =
        (
            bst.index !== undefined &&
            bst.index !== null
        )
            ? bst.index
            : "—";


    const path =
        Array.isArray(bst.path) &&
        bst.path.length
            ? bst.path.join(" → ")
            : topic;


    return `

        <div class="result-module">

            <div class="module-name">

                <span>03</span>

                BST

            </div>


            <div class="module-value">

                <strong class="${
                    found
                        ? "result-green"
                        : "result-red"
                }">

                    ${
                        found
                            ? "LOCATED"
                            : "NOT FOUND"
                    }

                </strong>

                <small>

                    Topic:
                    ${escapeHTML(
                        titleCase(topic)
                    )}

                    <br>

                    File:
                    ${escapeHTML(file)}

                    · Index:
                    ${escapeHTML(index)}

                </small>


                <div class="bst-path-value">

                    Path:
                    ${escapeHTML(path)}

                </div>

            </div>

        </div>

    `;
}


// ============================================================
// TF-IDF + COSINE SIMILARITY
// ============================================================

function buildTFIDF(similarity) {

    if (
        !Array.isArray(similarity) ||
        similarity.length === 0
    ) {

        return `

            <div class="result-module">

                <div class="module-name">

                    <span>04</span>

                    TF-IDF

                </div>

                <div class="module-value">

                    <strong>
                        NO SIMILAR DOCUMENT
                    </strong>

                    <small>
                        No similarity results returned.
                    </small>

                </div>

            </div>

        `;
    }


    const top =
        similarity[0];


    const topScore =
        Number(
            top.score || 0
        ).toFixed(2);


    const rows =
        similarity
            .slice(0, 4)
            .map(item => {

                const value =
                    Math.max(
                        0,
                        Math.min(
                            100,
                            Number(
                                item.score
                            ) || 0
                        )
                    );


                return `

                    <div class="similar-mini-row">

                        <span>
                            ${escapeHTML(
                                item.topic
                            )}
                        </span>

                        <div class="mini-bar">

                            <i
                                style="
                                    width:${value}%;
                                "
                            ></i>

                        </div>

                        <b>
                            ${Number(
                                item.score || 0
                            ).toFixed(2)}%
                        </b>

                    </div>

                `;

            })
            .join("");


    return `

        <div class="result-module tfidf-module">

            <div class="module-name">

                <span>04</span>

                TF-IDF

            </div>


            <div class="module-value">

                <strong>
                    ${escapeHTML(
                        top.topic
                    )}
                </strong>

                <small>
                    Most relevant document:
                    ${topScore}% similarity
                </small>


                <div class="similar-mini-list">

                    ${rows}

                </div>

            </div>

        </div>

    `;
}


// ============================================================
// RELATED TOPICS
// ============================================================

function buildRelatedTopics(topics) {

    if (
        !Array.isArray(topics) ||
        topics.length === 0
    ) {

        return "";
    }


    return `

        <div class="related-result">

            <small>
                RELATED TOPICS
            </small>


            <div class="related-pills">

                ${
                    topics
                        .slice(0, 5)
                        .map(topic => `

                            <span>
                                ${escapeHTML(
                                    topic
                                )}
                            </span>

                        `)
                        .join("")
                }

            </div>

        </div>

    `;
}


// ============================================================
// COMPLETE RESULT
// ============================================================

function showResults(data, query) {

    const box =
        getAnalysisBox();


    if (!box) {

        console.error(
            "Ready to Analyze box not found."
        );

        return;
    }


    const kmp =
        data.kmp || {};


    const fuzzy =
        data.fuzzy || {};


    const bst =
        data.bst || {};


    const similarity =
        Array.isArray(
            data.similarity
        )
            ? data.similarity
            : [];


    const related =
        Array.isArray(
            data.relatedTopics
        )
            ? data.relatedTopics
            : [];


    const topic =
        fuzzy.topic ||
        bst.topic ||
        query;


    const exact =
        kmp.found === true;


    const mainMessage =
        exact

            ? "Exact topic found using KMP and located using BST."

            : `Exact match not found. Closest topic found using Edit Distance: ${titleCase(
                topic
            )}.`;


    // ========================================================
    // EVERYTHING INSIDE SAME READY TO ANALYZE BOX
    // ========================================================

    box.innerHTML = `

        <div class="analysis-result">


            <!-- RESULT HEADER -->

            <div class="result-main-header">

                <div class="result-success-icon">
                    ✓
                </div>


                <div class="result-heading">

                    <span class="result-label">
                        ${
                            exact
                                ? "SEARCH RESULT"
                                : "FUZZY SEARCH RESULT"
                        }
                    </span>


                    <h2>
                        ${escapeHTML(
                            titleCase(topic)
                        )}
                    </h2>


                    <p>
                        ${escapeHTML(
                            mainMessage
                        )}
                    </p>

                </div>


                <div class="
                    result-status
                    ${
                        exact
                            ? "status-green"
                            : "status-purple"
                    }
                ">

                    ${
                        exact
                            ? "EXACT MATCH"
                            : "FUZZY MATCH"
                    }

                </div>

            </div>


            <!-- SUMMARY -->

            <div class="result-summary">

                <div class="summary-item">

                    <span>
                        QUERY
                    </span>

                    <strong>
                        ${escapeHTML(query)}
                    </strong>

                </div>


                <div class="summary-item">

                    <span>
                        EDIT DISTANCE
                    </span>

                    <strong>
                        ${fuzzy.distance ?? "—"}
                    </strong>

                </div>


                <div class="summary-item">

                    <span>
                        DOCUMENT INDEX
                    </span>

                    <strong>
                        ${bst.index ?? "—"}
                    </strong>

                </div>


                <div class="summary-item">

                    <span>
                        SIMILARITY
                    </span>

                    <strong>
                        ${
                            similarity.length
                                ? Number(
                                    similarity[0].score || 0
                                  ).toFixed(2) + "%"
                                : "0.00%"
                        }
                    </strong>

                </div>

            </div>


            <!-- ALGORITHM CARDS -->

            <div class="algorithm-result-grid">

                ${buildKMP(
                    kmp,
                    query
                )}


                ${buildFuzzy(
                    fuzzy,
                    kmp,
                    query
                )}


                ${buildBST(
                    bst,
                    fuzzy,
                    query
                )}


                ${buildTFIDF(
                    similarity
                )}

            </div>


            <!-- RELATED TOPICS -->

            ${buildRelatedTopics(
                related
            )}

        </div>

    `;


    // Premium highlight
    highlightResult();

}


// ============================================================
// SEARCH
// ============================================================

async function performSearch() {

    const query =
        input.value.trim();


    if (!query) {

        input.focus();

        return;
    }


    // --------------------------------------------------------
    // GO TO SAME READY TO ANALYZE BOX
    // --------------------------------------------------------

    scrollToAnalysis();


    // --------------------------------------------------------
    // LOADING
    // --------------------------------------------------------

    showLoading();


    if (searchBtn) {

        searchBtn.disabled = true;

        searchBtn.innerHTML =
            "<span>ANALYZING</span><b>…</b>";
    }


    if (statusEl) {

        statusEl.textContent =
            "Running KMP · Edit Distance · BST · TF-IDF...";

    }


    // --------------------------------------------------------
    // CALL JAVA BACKEND
    // --------------------------------------------------------

    try {

        const response =
            await fetch(
                API +
                encodeURIComponent(
                    query
                ),
                {
                    method: "GET",
                    cache: "no-cache"
                }
            );


        if (!response.ok) {

            throw new Error(
                "Java Server Error: " +
                response.status
            );

        }


        const raw =
            await response.text();


        console.log(
            "JAVA RAW RESPONSE:",
            raw
        );


        let data;


        try {

            data =
                JSON.parse(raw);

        } catch (jsonError) {

            throw new Error(
                "Java server returned invalid JSON."
            );

        }


        console.log(
            "JAVA SEARCH RESULT:",
            data
        );


        // ----------------------------------------------------
        // SHOW RESULTS IN SAME BOX
        // ----------------------------------------------------

        showResults(
            data,
            query
        );


        // ----------------------------------------------------
        // STATUS
        // ----------------------------------------------------

        if (statusEl) {

            if (
                data.kmp &&
                data.kmp.found === true
            ) {

                statusEl.textContent =
                    "Exact Match found · KMP + BST + TF-IDF completed";

            } else {

                statusEl.textContent =
                    `Fuzzy Match completed · Edit Distance = ${
                        data.fuzzy?.distance ?? "—"
                    }`;

            }

        }

    }


    catch (error) {

        console.error(
            "SEARCH ERROR:",
            error
        );


        const box =
            getAnalysisBox();


        if (box) {

            box.innerHTML = `

                <div class="analysis-loading">

                    <div class="analysis-icon">
                        !
                    </div>


                    <h3>
                        Java Backend Error
                    </h3>


                    <p>
                        ${escapeHTML(
                            error.message
                        )}
                    </p>


                    <small>
                        Start SearchServer.java
                        on port 8080.
                    </small>

                </div>

            `;

        }


        if (statusEl) {

            statusEl.textContent =
                "Java backend connection failed.";

        }

    }


    // --------------------------------------------------------
    // RESET BUTTON
    // --------------------------------------------------------

    if (searchBtn) {

        searchBtn.disabled = false;

        searchBtn.innerHTML =
            "<span>SEARCH</span><b>↗</b>";

    }

}


// ============================================================
// SEARCH BUTTON CLICK
// ============================================================

if (searchBtn) {

    searchBtn.addEventListener(
        "click",
        performSearch
    );

}


// ============================================================
// ENTER KEY
// ============================================================

if (input) {

    input.addEventListener(
        "keydown",
        event => {

            if (
                event.key === "Enter"
            ) {

                performSearch();

            }

        }
    );

}


// ============================================================
// QUICK SEARCH BUTTONS
// ============================================================

document
    .querySelectorAll(
        ".quick-search button"
    )
    .forEach(button => {

        button.addEventListener(
            "click",
            () => {

                const query =
                    button.dataset.query;


                if (query) {

                    input.value =
                        query;

                    performSearch();

                }

            }
        );

    });


// ============================================================
// NO AUTOMATIC SEARCH
// ============================================================

console.log(
    "Smart Pattern Search ready."
);