const searchInput = document.getElementById("search-input");
const articlesList = document.getElementById("articles-list");
const checkForNewArticleDuration = 1000;
searchInput && searchInput.addEventListener("keyup", search);
let updateTimer;

async function search() {
    if (! searchInput) return;
    let searchInputValue = searchInput.value
    if (! searchInputValue) {
        await getAllArticle();
        startUpdate();
        return;
    } else {
        stopUpdate();
    }
    try {
        const response = await fetch("/index/search-element/" + searchInputValue, {
            method: 'GET',
            mode: 'no-cors',
        });
        const list = await response.json();
        createArticles(list);
    } catch (err) {
        console.log("error : " + err)
    }
}

const getAllArticle = async () => {
    try {
        const response = await fetch("/index/search-element/all-9893", {
            method: 'GET',
            mode: 'no-cors',
        });
        const list = await response.json();
        if (list.length === articlesList.childElementCount) return;
        createArticles(list);
    } catch (err) {
        console.log("error : " + err)

    }
}

function createArticles(list) {
    articlesList.innerHTML = "";
    for (let i in list) {
        if (!list[i].title) break;
        const li =document.createElement( 'LI');

        li.className = "image-container";
        li.style = "display: flex; flex-direction: column; align-items: center; justify-content: center"
        const img = document.createElement('IMG');
        img.height = 100;
        img.src= "/image/" + list[i].posterID;
        const a = document.createElement("a");
        a.className="image-title";
        a.innerText = list[i].title
        const a2 = document.createElement("a");
        a2.className ="delete-image-form"
        const button = document.createElement("button");
        a2.href = "/index/article/" + list[i].id
        button.className = "danger-btn";
        button.type="submit"
        button.innerText = "Read more..."
        a2.appendChild(button)
        li.appendChild(img)
        li.appendChild(a)
        li.appendChild(a2)
        articlesList.appendChild(li);
    }
}

function startUpdate() {
    if (! updateTimer) {
        updateTimer = setInterval(getAllArticle, checkForNewArticleDuration);
    }
}

function stopUpdate() {
    if (updateTimer) {
        clearInterval(updateTimer);
        updateTimer = undefined;
    }
}

startUpdate();