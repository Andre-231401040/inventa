function downArrowIconHandler(downArrowIcon) {
  downArrowIcon.classList.toggle("rotate-180");
  downArrowIcon.classList.toggle("fill-[#bababa]");
  downArrowIcon.classList.toggle("fill-[#4f46e5]");
}

const dropdownButtons = document.querySelectorAll(".dropdownButtons");

dropdownButtons.forEach((dropdownBtn) => {
  const dropdownList = dropdownBtn.parentElement.querySelector("#dropdownList");
  const dropdownItems = dropdownBtn.parentElement.querySelectorAll(".option");
  const downArrowIcon = dropdownBtn.parentElement.querySelector("#downArrowIcon");

  dropdownBtn.addEventListener("click", (e) => {
    e.preventDefault();
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
    if (!dropdownBtn.parentElement.contains(e.target) && !dropdownList.classList.contains("hidden")) {
      dropdownList.classList.toggle("hidden");
      dropdownBtn.classList.toggle("border-[rgba(0,0,0,0.1)]");
      dropdownBtn.classList.toggle("border-[#4f46e5]");
      dropdownBtn.classList.toggle("text-[#bababa]");
      dropdownBtn.classList.toggle("text-[#4f46e5]");
      downArrowIconHandler(downArrowIcon);
    }
  });
});
