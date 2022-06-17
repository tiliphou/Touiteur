const searchInput = document.getElementById("search-input")
const ArticlesList = document.getElementById("articles-list")
const SEARCH_URL = "http://localhost:8091/index/search-element/";
searchInput.addEventListener("keyup", search)

async function search() {
    let searchInputValue = searchInput.value
    try {
        const response = await fetch(SEARCH_URL + searchInputValue, {
            method: 'GEt',
            mode: 'no-cors',
        });
        const list = await response.json();
        ArticlesList.innerHTML = "";
        for (let i in list) {
            if (!list[i].title) break;
            const li =document.createElement( 'LI');

            li.className = "image-container";
            li.style = "display: flex; flex-direction: column; align-items: center; justify-content: center"
            const img = document.createElement('IMG');
            img.className = "image-element"
            img.src="http://localhost:8091/image/" + list[i].posterID;
            const a = document.createElement("a");
            a.className="image-title";
            a.innerText = list[i].title
            const a2 = document.createElement("a");
            a2.className ="delete-image-form"
            const button = document.createElement("button");
            a2.href = "http://localhost:8091/index/article/" + list[i].id
            button.className = "danger-btn";
            button.type="submit"
            button.innerText = "Read more..."
            a2.appendChild(button)
            li.appendChild(img)
            li.appendChild(a)
            li.appendChild(a2)
            ArticlesList.appendChild(li);
            console.log(list[i])
        }
    } catch (err) {
        console.log("error : " + err)
    }

}