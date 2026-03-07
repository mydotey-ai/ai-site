// SCSS 模块声明
declare module '*.scss' {
  const content: string
  export default content
}

// CSS 模块声明
declare module '*.css' {
  const content: { [className: string]: string }
  export default content
}