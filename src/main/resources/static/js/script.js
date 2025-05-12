function downArrowIconHandler(downArrowIcon) {
  downArrowIcon.classList.toggle("rotate-180");
  downArrowIcon.classList.toggle("fill-[#bababa]");
  downArrowIcon.classList.toggle("fill-[#4f46e5]");
}

const dropdownBtn = document.getElementById("dropdownButton");
const dropdownList = document.getElementById("dropdownList");
const dropdownItems = document.querySelectorAll("li");
const downArrowIcon = document.getElementById("downArrowIcon");

dropdownBtn.addEventListener("click", () => {
  dropdownList.classList.toggle("hidden");
  dropdownBtn.classList.toggle("border-[rgba(0,0,0,0.1)]");
  dropdownBtn.classList.toggle("border-[#4f46e5]");
  dropdownBtn.classList.toggle("text-[#bababa]");
  dropdownBtn.classList.toggle("text-[#4f46e5]");
  downArrowIconHandler(downArrowIcon);
});

dropdownItems.forEach((item) => {
  item.addEventListener("click", () => {
    dropdownBtn.childNodes[0].textContent = item.textContent;
    dropdownList.classList.toggle("hidden");
    dropdownBtn.classList.toggle("border-[rgba(0,0,0,0.1)]");
    dropdownBtn.classList.toggle("border-[#4f46e5]");
    dropdownBtn.classList.toggle("text-[#bababa]");
    dropdownBtn.classList.toggle("text-[#4f46e5]");
    downArrowIconHandler(downArrowIcon);
  });
});

document.addEventListener("click", (e) => {
  if (!dropdownBtn.parentElement.contains(e.target)) {
    dropdownList.classList.toggle("hidden");
    dropdownBtn.classList.toggle("border-[rgba(0,0,0,0.1)]");
    dropdownBtn.classList.toggle("border-[#4f46e5]");
    dropdownBtn.classList.toggle("text-[#bababa]");
    dropdownBtn.classList.toggle("text-[#4f46e5]");
    downArrowIconHandler(downArrowIcon);
  }
});
